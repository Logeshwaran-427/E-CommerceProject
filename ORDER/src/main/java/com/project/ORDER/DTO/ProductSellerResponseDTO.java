package com.project.ORDER.DTO;

import com.project.ORDER.Enums.ProductStatus;

public class ProductSellerResponseDTO {

    Long productId;
    Long sellerId;
    ProductStatus productStatus;

    public ProductSellerResponseDTO(Long productId, Long sellerId, ProductStatus productStatus) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.productStatus = productStatus;
    }
    
    public ProductSellerResponseDTO() {
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public ProductStatus getProductStatus() {
        return productStatus;
    }
    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
    
}
