package com.project.ORDER.DTO;

import com.project.ORDER.Enums.NotificationType;

public class OrderStatusNotification {
    
    Long userId;
    Long orderId;
    String message;
    NotificationType type;

    public OrderStatusNotification(Long userId, Long orderId, String message, NotificationType type) {
        this.userId = userId;
        this.orderId = orderId;
        this.message = message;
        this.type = type;
    }

    public OrderStatusNotification() {
    }

    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public NotificationType getType() {
        return type;
    }
    public void setType(NotificationType type) {
        this.type = type;
    }

    
}
