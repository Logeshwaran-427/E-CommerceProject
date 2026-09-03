package com.project.ORDER.DTO;

import com.project.ORDER.Enums.PaymentMethod;

public class PlaceDirectOrderRequest {

    Long productId;
    PaymentMethod paymentMethod;
    Integer quantity;
    Long addressId;

    public PlaceDirectOrderRequest() {
    }
    public PlaceDirectOrderRequest(Long productId, PaymentMethod paymentMethod, Integer quantity,Long addressId) {
        this.productId = productId;
        this.paymentMethod = paymentMethod;
        this.quantity = quantity;
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
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Long getAddressId() {
        return addressId;
    }
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    
}
