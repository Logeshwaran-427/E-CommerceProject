package com.project.ORDER.DTO;

import com.project.ORDER.Enums.PaymentMethod;

public class PaymentRequesDTO {

    Long orderId;
    Double amount;
    PaymentMethod paymentMethod;

    public PaymentRequesDTO(Long orderId, Double amount, PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public PaymentRequesDTO() {
    }
    
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    
}
