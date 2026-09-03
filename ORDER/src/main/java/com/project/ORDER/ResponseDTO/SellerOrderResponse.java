package com.project.ORDER.ResponseDTO;


import java.time.LocalDateTime;

import com.project.ORDER.Enums.OrderItemStatus;

public class SellerOrderResponse {

    Long orderId;
    Long orderItemId;
    Long productId;
    Integer quantity;
    Double price;
    OrderItemStatus orderItemStatus;
    LocalDateTime orderedAt;
    public SellerOrderResponse(Long orderId, Long orderItemId, Long productId, Integer quantity, Double price,
            OrderItemStatus orderItemStatus, LocalDateTime orderedAt) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.orderItemStatus = orderItemStatus;
        this.orderedAt = orderedAt;
    }
    public SellerOrderResponse() {
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Long getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public OrderItemStatus getOrderItemStatus() {
        return orderItemStatus;
    }
    public void setOrderItemStatus(OrderItemStatus orderItemStatus) {
        this.orderItemStatus = orderItemStatus;
    }
    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }
    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    
    
}

