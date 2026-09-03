package com.project.PAYMENT.DTO;

public class UserContext {

    Long sellerId;
    Long userId;
    String role;
    public UserContext(Long sellerId, Long userId, String role) {
        this.sellerId = sellerId;
        this.userId = userId;
        this.role = role;
    }
    public UserContext() {
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    
}
