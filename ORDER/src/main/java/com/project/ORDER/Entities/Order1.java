package com.project.ORDER.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.ORDER.Enums.OrderStatus;
import com.project.ORDER.Enums.OrderType;
import com.project.ORDER.Enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@Entity
public class Order1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long userId;
    Long addressId;
    Double totalAmount;
    @Enumerated (EnumType.STRING)
    OrderType orderType;
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    PaymentStatus paymentStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    List<OrderItem> orderItems=new ArrayList<>();

    public void addOrderItem(OrderItem item){
        orderItems.add(item);
        item.setOrder(this);
    }
    

    public Order1(Long userId, Double totalAmount, OrderType orderType, OrderStatus orderStatus, PaymentStatus paymentStatus,
            LocalDateTime createdAt,Long addressId, LocalDateTime updatedAt, List<OrderItem> orderItems) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderItems = orderItems;
        this.orderType=orderType;
        this.addressId=addressId;
    }

    public Order1() {
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getAddressId() {
        return addressId;
    }


    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
    
}
