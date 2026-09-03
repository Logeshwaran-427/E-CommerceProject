package com.project.ORDER.DTO;

public class ProductSellerRequestDTO {

    Long productId;
    Long sellerId;

    public ProductSellerRequestDTO(Long productId, Long sellerId) {
        this.productId = productId;
        this.sellerId = sellerId;
    }

    public ProductSellerRequestDTO() {
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
    
}
