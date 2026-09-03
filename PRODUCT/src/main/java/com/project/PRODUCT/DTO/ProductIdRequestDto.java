package com.project.PRODUCT.DTO;

import java.util.List;

public class ProductIdRequestDto {
    List<Long> productIds;

    public ProductIdRequestDto(List<Long> productIds) {
        this.productIds = productIds;
    }

    public ProductIdRequestDto() {
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
    
}
