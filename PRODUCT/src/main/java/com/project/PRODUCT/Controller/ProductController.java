package com.project.PRODUCT.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.PRODUCT.DTO.CategoryDTO;
import com.project.PRODUCT.DTO.ProductDTO;
import com.project.PRODUCT.DTO.ProductIdRequestDto;
import com.project.PRODUCT.DTO.ProductPageResponse;
import com.project.PRODUCT.DTO.ProductResponse;
import com.project.PRODUCT.Entity.Categories;
import com.project.PRODUCT.Entity.Product;
import com.project.PRODUCT.ResponseDTO.ProductResponseDTO;
import com.project.PRODUCT.Service.ProductService;

import jakarta.validation.Valid;





@RestController
@RequestMapping("/product")
public class ProductController {

    final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/createProduct")
    @PreAuthorize("hasAnyRole('PRODUCT_OWNER','ADMIN')")
    public String addProduct(@RequestBody @Valid ProductDTO product) {
        return productService.addProduct(product);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createCategory")
    public String addCategory(@RequestBody @Valid  CategoryDTO categories) {
        return productService.addCategory(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getProducts")
    public ProductPageResponse getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    @GetMapping("/getCategory")
    public List<Categories> getCategory() {
        return productService.getCategories();
    }
    
    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER','ADMIN')")
    @GetMapping("/getProduct/{id}")
    public ProductResponse getMethodName(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @GetMapping("/getProductByBrand/")
    public ProductPageResponse productByBrand(@RequestParam String brand,Pageable pageable) {
        return productService.getByBrand(brand,pageable);
    }
    
    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @GetMapping("getProductByCategory/{id}")
    public ProductPageResponse productByCategory(@PathVariable Long id,Pageable pageable) {
        return productService.getByCategory(id,pageable);
    }
    
    @PreAuthorize("hasRole('PRODUCT_OWNER')")
    @GetMapping("/getMyProducts")
    public ProductPageResponse getMethodName(Pageable pageable) {
        return productService.getMyProducts(pageable);
    }

    @PreAuthorize("hasRole('PRODUCT_OWNER')")
    @GetMapping("/myProducts/{id}")
    public Product myProductsByID(@PathVariable Long id) {
        return productService.getSellerProductsById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/getActiveProducts")
    public Page<Product> getActiveProducts(Pageable pageable) {
        return productService.getActiveProducts(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','PRODUCT_OWNER')")
    @GetMapping("/searchByKeyword")
    public Page<Product> searchByKeyword(@RequestParam String keyword,Pageable pageable) {
        return productService.searchProduct(keyword,pageable);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/updateCategory/{id}")
    public String categoryUpdate(@PathVariable Long id, @RequestBody CategoryDTO category) {
        return productService.updateCategory(id, category);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/updateProduct/{id}")
    public String productUpdate(@PathVariable Long id, @RequestBody ProductDTO productDTO) {  
        return productService.updateProduct(id, productDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/deleteProduct")
    public String productDelete(@RequestParam Long id){
        return productService.deleteProduct(id);
    }

    @PreAuthorize("hasRole('PRODUCT_OWNER')")
    @DeleteMapping("/seller/deleteMyProduct")
    public String deleteMyProduct(@RequestParam Long id){
        return productService.deleteMyProduct(id);
    }  
    
    
    @PreAuthorize("hasRole('PRODUCT_OWNER')")
    @PutMapping("seller/updateMyProduct/{id}")
    public String putMethodName(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateMyProduct(id, productDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','PRODUCT_OWNER')")
    @PostMapping("/getByProductIds")
    public List<ProductResponseDTO> getByProductIds(@RequestBody ProductIdRequestDto productIds) {
        return productService.checkProductIds(productIds.getProductIds());
    }
    
    
    
}
