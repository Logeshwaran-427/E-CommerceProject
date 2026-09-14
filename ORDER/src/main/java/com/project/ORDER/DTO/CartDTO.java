package com.project.ORDER.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CartDTO {

    @NotNull (message = "Product Id field is required")
    Long productId; 
    @Positive (message = "Field should be positive")
    Integer quantity;

    public CartDTO(@NotBlank(message = "Product Id field is required") Long productId,
            @Positive(message = "Field should be positive") Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartDTO() {
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    
}
