package com.project.INVENTORY.DTO;

import jakarta.validation.constraints.Positive;

public class InventoryRequestDTO {
    @Positive (message = "Enter the product id and it should be positive")
    Long productId;
    @Positive (message = "Quantity should be positive")
    Integer stock;

    public InventoryRequestDTO(Long productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }

    public InventoryRequestDTO() {
    }
    
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
