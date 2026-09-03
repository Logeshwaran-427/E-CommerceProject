package com.project.INVENTORY.Entity;

import java.time.LocalDateTime;

import com.project.INVENTORY.Enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
public class InventoryTransactions {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String remarks;

    private Long performedBy;

    private LocalDateTime createdAt;

    public InventoryTransactions() {
    }

    public InventoryTransactions(Long productId, Integer quantity, TransactionType transactionType, String remarks,
            Long performedBy, LocalDateTime createdAt) {
        this.productId = productId;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.remarks = remarks;
        this.performedBy = performedBy;
        this.createdAt = createdAt;
    }

    @PrePersist
    public void createdDate(){
        this.createdAt=LocalDateTime.now();
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
