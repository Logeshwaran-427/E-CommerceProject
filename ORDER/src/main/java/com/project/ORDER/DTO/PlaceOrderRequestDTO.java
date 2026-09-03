package com.project.ORDER.DTO;

import com.project.ORDER.Enums.PaymentMethod;

public class PlaceOrderRequestDTO {
    Long productId;
    PaymentMethod paymentMethod;
    Long addressId;
    
    public PlaceOrderRequestDTO() {
    }
    public PlaceOrderRequestDTO(Long productId, PaymentMethod paymentMethod,Long addressId) {
        this.productId = productId;
        this.paymentMethod = paymentMethod;
        this.addressId=addressId;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public Long getAddressId() {
        return addressId;
    }
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    
}
