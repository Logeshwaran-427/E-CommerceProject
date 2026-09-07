package com.project.AUTHSERVICE.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.AUTHSERVICE.Enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class UserDet implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank (message = "Enter your name")
    String username;
    @NotBlank(message = "Email is required")
    @Email (message = "Enter valid email")
    String email;
    @NotBlank(message = "Password is required")
    @Column (nullable = false)
    String password;
    @NotBlank(message = "Phone number is required")
    String phoneNumber;
    Boolean isActive;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    @Enumerated(EnumType.STRING)
    RoleEnum role;

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }



    public UserDet(String username, String email, String password, String phoneNumber, Boolean isActive,
            LocalDateTime createdDate, LocalDateTime updatedDate, RoleEnum role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.role=role;
    }

    public UserDet() {
    }

    @PrePersist
    public void setValues(){

        this.isActive=true;

        LocalDateTime t1=LocalDateTime.now();
        this.createdDate=t1;
        this.updatedDate=t1;
    }

    @PreUpdate
    public void update(){
        this.updatedDate=LocalDateTime.now();
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role));
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(isActive);
     }
    


    
}
