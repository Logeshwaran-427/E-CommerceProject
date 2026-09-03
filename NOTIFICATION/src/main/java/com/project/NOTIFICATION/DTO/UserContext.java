package com.project.NOTIFICATION.DTO;


public class UserContext {
    String role;
    Long userId;

    public UserContext(String role, Long userId) {
        this.role = role;
        this.userId = userId;
    }

    public UserContext() {
    }
    
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
}
