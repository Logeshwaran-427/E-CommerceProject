package com.project.ORDER.Entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.ORDER.Enums.OrderItemStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long productId;
    Integer quantity;
    Double price;
    Double subTotal;
    Long sellerId;
    @Enumerated(EnumType.STRING)
    OrderItemStatus orderItemStatus;
    LocalDateTime orderedAt;

    @PrePersist
    public void setOrderedTime(){
        this.orderedAt=LocalDateTime.now();
    }


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="orderId")
    Order1 order;

    public OrderItem(Long productId,LocalDateTime orderedAt,Long sellerId,OrderItemStatus orderItemStatus, Integer quantity, Double price, Double subTotal, Order1 order) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
        this.order = order;
        this.orderItemStatus=orderItemStatus;
        this.sellerId=sellerId;
        this.orderedAt=orderedAt;
    }

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Order1 getOrder() {
        return order;
    }

    public void setOrder(Order1 order) {
        this.order = order;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
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
