package com.project.PRODUCT.ResponseDTO;

import com.project.PRODUCT.Enums.ProductStatus;

public class ProductResponseDTO {

    Long id;
    double price;
    ProductStatus status;
    Long sellerId;

    public ProductResponseDTO(Long id, double price, ProductStatus status,Long sellerId) {
        this.id = id;
        this.price = price;
        this.status = status;
        this.sellerId=sellerId;
    }
    
    public ProductResponseDTO() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public ProductStatus getStatus() {
        return status;
    }
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    
}
