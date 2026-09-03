package com.project.INVENTORY.DTO;

public class InventoryRequestDTO {
    Long productId;
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
