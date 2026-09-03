package com.project.ORDER.DTO;

import com.project.ORDER.Enums.InventoryStatus;

public class InventoryDTO {
    Long id;
    Long productId;
    Integer availableQuantity;
    InventoryStatus status;

    public InventoryDTO(Long id, Long productId, Integer availableQuantity, InventoryStatus status) {
        this.id = id;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.status = status;
    }

    public InventoryDTO() {
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    public InventoryStatus getStatus() {
        return status;
    }
    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    
}
