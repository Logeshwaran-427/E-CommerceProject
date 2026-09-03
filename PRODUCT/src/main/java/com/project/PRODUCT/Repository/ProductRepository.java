package com.project.PRODUCT.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.PRODUCT.Entity.Categories;
import com.project.PRODUCT.Entity.Product;
import com.project.PRODUCT.Enums.ProductStatus;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByNameIgnoreCaseAndSellerId(String name,Long sellerId);

    Page<Product> findAllByBrandContainingIgnoreCaseAndStatusNot(String brand, ProductStatus status,Pageable pageable);

    Optional<Product> findByIdAndStatusNot(Long id, ProductStatus status);

    Page<Product> findAllByCategoryAndStatusNot(Categories categories, ProductStatus status,Pageable pageable);

    Page<Product> findAllBySellerId(Long id,Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCaseAndStatusNot(String name, ProductStatus status,Pageable pageable);

    Page<Product> findAllByStatusNot(ProductStatus status,Pageable pageable);

    boolean existsByNameAndDescriptionIgnoreCaseAndSellerIdAndIdNot(String name, String description, Long sellerId,Long id);

    List<Product> findByIdInAndStatusNot(List<Long> productIds,ProductStatus status);
    
}
