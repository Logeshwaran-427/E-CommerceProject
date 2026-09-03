package com.project.ORDER.DTO;

import java.util.List;

public class ProductIdsRequestDTO {
    List<Long> productIds;

    public ProductIdsRequestDTO(List<Long> productIds) {
        this.productIds = productIds;
    }

    public ProductIdsRequestDTO() {
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
