package com.project.ORDER.DTO;

import com.project.ORDER.Enums.ProductStatus;

public class ProductResponseDTO {
    Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    // Long userId;
    Double price;
    ProductStatus status;
    Long sellerId;

    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public ProductResponseDTO(Long id, Double price,ProductStatus status,Long sellerId) {
        this.id = id;
        // this.userId = userId;
        this.price = price;
        this.status=status;
        this.sellerId=sellerId;
    }
    public ProductResponseDTO() {
    }
    // public Long getProductId() {
    //     return productId;
    // }
    // public void setProductId(Long productId) {
    //     this.productId = productId;
    // }
    // public Long getUserId() {
    //     return userId;
    // }
    // public void setUserId(Long userId) {
    //     this.userId = userId;
    // }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public ProductStatus getStatus() {
        return status;
    }
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
}
