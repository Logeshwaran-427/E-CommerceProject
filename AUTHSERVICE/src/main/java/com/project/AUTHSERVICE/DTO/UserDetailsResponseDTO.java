package com.project.AUTHSERVICE.DTO;

import java.time.LocalDateTime;

import com.project.AUTHSERVICE.Enums.RoleEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class UserDetailsResponseDTO {

    Long id;  
    String username;   
    String email;
    String phoneNumber;
    Boolean isActive;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    RoleEnum role;

    public UserDetailsResponseDTO(Long id, String username, String email, String phoneNumber, Boolean isActive,
            LocalDateTime createdDate, LocalDateTime updatedDate, RoleEnum role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.role = role;
    }

    public UserDetailsResponseDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    public RoleEnum getRole() {
        return role;
    }
    public void setRole(RoleEnum role) {
        this.role = role;
    }

    
}
