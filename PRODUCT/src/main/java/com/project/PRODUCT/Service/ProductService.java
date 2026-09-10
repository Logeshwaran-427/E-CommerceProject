package com.project.PRODUCT.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.PRODUCT.Client.ProductClient;
import com.project.PRODUCT.DTO.CategoryDTO;
import com.project.PRODUCT.DTO.ProductDTO;
import com.project.PRODUCT.DTO.ProductPageResponse;
import com.project.PRODUCT.DTO.ProductResponse;
import com.project.PRODUCT.DTO.UserContext;
import com.project.PRODUCT.Entity.Categories;
import com.project.PRODUCT.Entity.Product;
import com.project.PRODUCT.Enums.ProductStatus;
import com.project.PRODUCT.ExceptionHandling.AuthorizationException;
import com.project.PRODUCT.ExceptionHandling.BadRequestException;
import com.project.PRODUCT.ExceptionHandling.DuplicateException;
import com.project.PRODUCT.ExceptionHandling.ResourceNotFoundException;
import com.project.PRODUCT.Repository.CategoryRepository;
import com.project.PRODUCT.Repository.ProductRepository;
import com.project.PRODUCT.ResponseDTO.ProductResponseDTO;
import com.project.PRODUCT.UtilityClasses.JwtUtility;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ProductService {

    final ProductRepository productRepository;

    final CategoryRepository categoryRepository;

    final ProductClient productClient;

    final HttpServletRequest request;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    ProductService(ProductClient productClient, CategoryRepository categoryRepository, ProductRepository productRepository, HttpServletRequest request) {
        this.productClient = productClient;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.request = request;
    }

    //List all products applicable for ADMIN

    @Cacheable(value = "AllProducts")
    public ProductPageResponse getProducts(Pageable pageable){
        log.info("Admin requested product list. Page={}", pageable.getPageNumber());

        Page<Product> products=productRepository.findAll(pageable);
        List<ProductResponse> productDetails=new ArrayList<>();

        for(Product product:products){
            ProductResponse productResponse=new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setBrand(product.getBrand());
            productResponse.setPrice(product.getPrice());
            productResponse.setCategory(product.getCategory().getName());
            productResponse.setStatus(product.getStatus());
            productDetails.add(productResponse);
        }

        return new ProductPageResponse(productDetails,products.getNumber(),products.getTotalPages(),products.getTotalElements());
    }


    //Add category applicable for admin

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllCategory",allEntries = true)})
    public String addCategory(CategoryDTO categories){
        log.info("Request received to create category '{}'", categories.getName());

        if(categoryRepository.existsByNameIgnoreCase(categories.getName())){
            throw new BadRequestException("Category already present");
        }
        Categories category=new Categories();
        category.setName(categories.getName());
        category.setDescription(categories.getDescription());
        log.info("Category '{}' created successfully", categories.getName());
        categoryRepository.save(category);
        return "Category added";

    }


    // get category applicable for sellers and admin 

    @Cacheable(value = "AllCategory")
    public List<Categories> getCategories(){
        log.info("Fetching all categories");
        return categoryRepository.findAll();
    }


    // add product by seller

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllProducts",allEntries = true),
    @CacheEvict(value = "productById",allEntries = true),
    @CacheEvict(value = "productByBrand",allEntries = true),
    @CacheEvict(value = "productByCategory",allEntries = true),
    @CacheEvict(value = "searchByName",allEntries = true),
    @CacheEvict(value = "ActiveProducts",allEntries = true)
    })
    public String addProduct(ProductDTO product){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext)authentication.getDetails();
        String role=userContext.getRole();
        Long sellerId=null;
        Long userId=userContext.getUserId();

        if(role.equals("PRODUCT_OWNER")){
            sellerId=userContext.getSellerId();
            if(sellerId==null){
            throw new AuthorizationException("SellerId not found");
            }
        }
        
        log.info("User {} with role {} is creating product '{}'",userId,role,product.getName());
        if(sellerId!=null){
            if(productRepository.existsByNameIgnoreCaseAndSellerId(product.getName(), sellerId)){
            throw new BadRequestException("Product already exists");
            }
        }
        else{
            if(productRepository.existsByNameIgnoreCaseAndSellerIdIsNull(product.getName())){
                throw new BadRequestException("Product already exists");
            }
        }

        Categories category=categoryRepository.findById(product.getCategoryId()).orElseThrow(()->new ResourceNotFoundException("Invalid category ID"));

        Product product1=new Product();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setBrand(product.getBrand());
        product1.setPrice(product.getPrice());
        product1.setCategory(category);
        product1.setSellerId(sellerId);
        product1.setCreatedBy(userId);
        product1.setStatus(ProductStatus.ACTIVE);
        String generateSku=product.getBrand()+"-"+product.getName()+"-"+UUID.randomUUID().toString().substring(0, 4);
        product1.setSku(generateSku);
        
        productRepository.save(product1);
            log.info("Product {} created successfully with id={} by user={}",product1.getName(),product1.getId(),userId);
        return "Product added successfully";

    }


    //get product by id

    @Cacheable(value = "productById",key = "#id")
    public ProductResponse getById(Long id){
        log.info("Fetching product {}", id);
        
        Product product1=productRepository.findByIdAndStatusNot(id, ProductStatus.INACTIVE).orElseThrow(()->new ResourceNotFoundException("Invalid ID. Please provide the appropraie id"));
        ProductResponse productResponse=new ProductResponse();
        productResponse.setId(product1.getId());
        productResponse.setName(product1.getName());
        productResponse.setDescription(product1.getDescription());
        productResponse.setBrand(product1.getBrand());
        productResponse.setPrice(product1.getPrice());
        productResponse.setCategory(product1.getCategory().getName());
        productResponse.setStatus(product1.getStatus());
        log.info("Product {} fetched successfully", id);
        return productResponse;
        
        }
    
    //get product by brand
    @Cacheable(value = "productByBrand")
    public ProductPageResponse getByBrand(String name,Pageable pageable){
        log.info("Searching products by brand '{}'", name);
        Page<Product> products=productRepository.findAllByBrandContainingIgnoreCaseAndStatusNot(name, ProductStatus.INACTIVE,pageable);
        List<ProductResponse> productDetails=new ArrayList<>();

        for(Product product:products){
            ProductResponse productResponse=new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setBrand(product.getBrand());
            productResponse.setPrice(product.getPrice());
            productResponse.setCategory(product.getCategory().getName());
            productResponse.setStatus(product.getStatus());
            productDetails.add(productResponse);
        }

        return new ProductPageResponse(productDetails,products.getNumber(),products.getTotalPages(),products.getTotalElements());
    }


    //get product by category

    @Cacheable(value = "productByCategory")
    public ProductPageResponse getByCategory(Long id,Pageable pageable){
        log.info("Fetching products for category {}", id);
        Categories category=categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No category found"));
        Page<Product> products=productRepository.findAllByCategoryAndStatusNot(category,ProductStatus.INACTIVE,pageable);
        List<ProductResponse> productDetails=new ArrayList<>();

        for(Product product:products){
            ProductResponse productResponse=new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setBrand(product.getBrand());
            productResponse.setPrice(product.getPrice());
            productResponse.setCategory(product.getCategory().getName());
            productResponse.setStatus(product.getStatus());
            productDetails.add(productResponse);
        }

        return new ProductPageResponse(productDetails,products.getNumber(),products.getTotalPages(),products.getTotalElements());
  
    }


    //Seller views his product

    public ProductPageResponse getMyProducts(Pageable pageable){
        
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Long sellerId=(Long) auth.getDetails();
        log.info("Seller {} requested own products", sellerId);

        Page<Product> products=productRepository.findAllBySellerId(sellerId, pageable);
        List<ProductResponse> productDetails=new ArrayList<>();

        for(Product product:products){
            ProductResponse productResponse=new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setDescription(product.getDescription());
            productResponse.setBrand(product.getBrand());
            productResponse.setPrice(product.getPrice());
            productResponse.setCategory(product.getCategory().getName());
            productResponse.setStatus(product.getStatus());
            productDetails.add(productResponse);
        }

        return new ProductPageResponse(productDetails,products.getNumber(),products.getTotalPages(),products.getTotalElements());
  
    }


    // Seller updates his product

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllProducts"),
                        @CacheEvict(value = "productById",allEntries = true),
                        @CacheEvict(value = "productByBrand",allEntries = true),
                        @CacheEvict(value = "productByCategory",allEntries = true),
                        @CacheEvict(value = "searchByName",allEntries = true),
                        @CacheEvict(value = "ActiveProducts",allEntries = true)
                        })
    public String updateMyProduct(Long id,ProductDTO productDTO){
        Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        log.info("Seller {} updating product {}", sellerId, id);
        if(!product.getSellerId().equals(sellerId)){
            throw new AuthorizationException("You are not the owner for this product");
        }

        if(productDTO.getName()==null || productDTO.getDescription()==null || 
        productDTO.getPrice()==null || productDTO.getBrand() ==null ){
            throw new BadRequestException("You must fill all the required fields");
        }

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        Categories category = categoryRepository
        .findById(productDTO.getCategoryId())
        .orElseThrow(()-> new ResourceNotFoundException("Category ID not found"));
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);
        log.info("Product {} updated successfully by seller {}", id, sellerId);
        return "Product updated successfully";

    }


    //Seller deletes his product

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllProducts"),
    @CacheEvict(value = "productById",allEntries = true),
    @CacheEvict(value = "productByBrand",allEntries = true),
    @CacheEvict(value = "productByCategory",allEntries = true),
    @CacheEvict(value = "searchByName",allEntries = true),
    @CacheEvict(value = "ActiveProducts",allEntries = true)
    })
    public String deleteMyProduct(Long id){

        Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not found"));
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        log.info("Seller {} requested deletion of product {}", sellerId, id);
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        if(!product.getSellerId().equals(sellerId)){
            throw new AuthorizationException("You are not the owner for this product");
        }
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
        productClient.deleteInventory(id, token,corrId);
        log.info("Product {} marked INACTIVE", id);
        
        return "Deleted Successfully";


    }


    // Admin deletes the product
    
    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllProducts"),
    @CacheEvict(value = "productById",allEntries = true),
    @CacheEvict(value = "productByBrand",allEntries = true),
    @CacheEvict(value = "productByCategory",allEntries = true),
    @CacheEvict(value = "searchByName",allEntries = true),
    @CacheEvict(value = "ActiveProducts",allEntries = true)
    })
    public String deleteProduct (Long id){
        log.info("Admin deleting product {}", id);
        Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not found"));
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
        productClient.deleteInventory(id, token,corrId);
        log.info("Product {} deleted by admin", id);
        return "Deleted Successfully";
    }


    // Admin updates the product

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllProducts"),
    @CacheEvict(value = "productById",allEntries = true),
    @CacheEvict(value = "productByBrand",allEntries = true),
    @CacheEvict(value = "productByCategory",allEntries = true),
    @CacheEvict(value = "searchByName",allEntries = true),
    @CacheEvict(value = "ActiveProducts",allEntries = true)
    })
    public String updateProduct(Long id, ProductDTO productDTO){
        log.info("Admin updating product {}", id);
        Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not found"));
        if(productDTO.getName()==null || productDTO.getDescription()==null || 
        productDTO.getPrice()==null || productDTO.getBrand() ==null ){
            throw new BadRequestException("You must fill all the required fields");
        }
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        // product.setStockQuantity(productDTO.getStockQuantity());
        Categories category = categoryRepository
        .findById(productDTO.getCategoryId())
        .orElseThrow(()-> new ResourceNotFoundException("Category ID not found"));
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);
        productRepository.save(product);
        log.info("Product {} updated successfully by admin", id);
        return "Product updated successfully";

    }

    // Admin updates the category

    @Transactional
    @Caching(evict = {@CacheEvict(value = "AllCategory",allEntries = true)})
    public String updateCategory(Long id, CategoryDTO categories){
        log.info("Updating category {}", id);
        Categories category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category not found!"));
        category.setName(categories.getName());
        category.setDescription(categories.getDescription());
        categoryRepository.save(category);
        log.info("Category {} updated successfully", id);
        return "Category updated";
    }

    //search by keywords

    @Cacheable(value = "searchByName")
    public Page<Product> searchProduct(String name,Pageable pageable){
        log.info("Searching products using keyword '{}'", name);
        return productRepository.findAllByNameContainingIgnoreCaseAndStatusNot(name, ProductStatus.INACTIVE,pageable);

    }

    // get ACTIVE and OUT_OF_STOCK products
    @Cacheable(value = "ActiveProducts")
    public Page<Product> getActiveProducts(Pageable pageable){
        log.info("Fetching active products");
        return productRepository.findAllByStatusNot(ProductStatus.INACTIVE,pageable);
    }

    //seller gets his product by id

    public Product getSellerProductsById(Long id){
        Product product=productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product Not found"));
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        Long sellerId=(Long) auth.getDetails();
        log.info("Seller {} requested product {}", sellerId, id);
        if(!product.getSellerId().equals(sellerId)){
            throw new AuthorizationException("You are not the owner for this product");
        }
        log.info("Product {} returned to seller {}", id, sellerId);
        return product;
    }


    public List<ProductResponseDTO> checkProductIds(List<Long> productIds){
        log.info("Validating {} product ids", productIds.size());
        List<Product> products=productRepository.findByIdInAndStatusNot(productIds, ProductStatus.INACTIVE);
        if(products.size()==0){
            throw new ResourceNotFoundException("No products found");
        }

        Set<Long> foundedIds=products.stream().map(ids->ids.getId()).collect(Collectors.toSet());
        List<Long> missingIds=productIds.stream().filter(ids->!foundedIds.contains(ids)).toList();
    
        if(missingIds.size()>0){
            throw new ResourceNotFoundException("Invalid or inactive product IDs: " + missingIds);
        }

        List<ProductResponseDTO> productResponseDTOs=new ArrayList<>();
        for(Product i: products){
            ProductResponseDTO response=new ProductResponseDTO();
            response.setId(i.getId());
            response.setPrice(i.getPrice());
            response.setStatus(i.getStatus());
            response.setSellerId(i.getSellerId());
            productResponseDTOs.add(response);
        }
        log.info("All product ids validated successfully");

        return productResponseDTOs;

    }
    
    
}
