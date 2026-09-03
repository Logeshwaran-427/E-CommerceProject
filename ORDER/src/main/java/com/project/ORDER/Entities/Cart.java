package com.project.ORDER.Entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @JsonBackReference
    @OneToMany(mappedBy = "cart",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    List<CartItems> cartItems;

    public Cart(Long userId, LocalDateTime createdAt, LocalDateTime updatedAt, List<CartItems> cartItems) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cartItems = cartItems;
    }

    public Cart() {
    }

    @PrePersist
    public void creationDate(){
        LocalDateTime now=LocalDateTime.now();
        this.createdAt=now;
        this.updatedAt=now;
    }

    @PreUpdate
    public void updatedate(){
        this.updatedAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CartItems> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItems> cartItems) {
        this.cartItems = cartItems;
    }
}
