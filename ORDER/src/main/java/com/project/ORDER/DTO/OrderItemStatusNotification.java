package com.project.ORDER.DTO;

import com.project.ORDER.Enums.NotificationType;

public class OrderItemStatusNotification {
    Long orderId;
    Long orderItemId;
    String message;
    Long userId;
    NotificationType type;
    Long productId;
    
    public OrderItemStatusNotification(Long orderId, Long orderItemId, String message, Long userId,
            NotificationType type, Long productId) {
        this.orderId = orderId;
        this.orderItemId = orderItemId;
        this.message = message;
        this.userId = userId;
        this.type = type;
        this.productId = productId;
        
    }
    public OrderItemStatusNotification() {
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
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
       this.message = message;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    
}
