package com.project.INVENTORY.DTO;

import com.project.INVENTORY.Enums.ProductStatus;

public class ProductClient {

    Long id;
    Long sellerId;
    ProductStatus status;

    public ProductClient() {
    }

    public ProductClient(Long id, Long sellerId, ProductStatus status) {
        this.id = id;
        this.sellerId = sellerId;
        this.status = status;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long seller_id) {
        this.sellerId = seller_id;
    }
    public ProductStatus getStatus() {
        return status;
    }
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
}
