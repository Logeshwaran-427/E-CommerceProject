package com.project.INVENTORY.DTO;

public class UserContext {
    Long userId;
    Long sellerId;
    String role;
    public UserContext(Long userId, Long sellerId, String role) {
        this.userId = userId;
        this.sellerId = sellerId;
        this.role = role;
    }
    public UserContext() {
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
