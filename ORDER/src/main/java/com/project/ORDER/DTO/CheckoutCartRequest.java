package com.project.ORDER.DTO;

import com.project.ORDER.Enums.PaymentMethod;

public class CheckoutCartRequest {

    PaymentMethod paymentMethod;
    Long addressId;

   

    public CheckoutCartRequest(PaymentMethod paymentMethod,Long addressId) {
        this.paymentMethod = paymentMethod;
        this.addressId=addressId;
    }

    public CheckoutCartRequest() {
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
