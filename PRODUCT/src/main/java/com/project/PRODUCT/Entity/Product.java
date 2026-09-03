package com.project.PRODUCT.Entity;

import java.time.LocalDateTime;

import com.project.PRODUCT.Enums.ProductStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"name","description","seller_id"}
        )
    }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;
    Double price;
    // Integer stockQuantity;
    Long sellerId;
    @Enumerated(EnumType.STRING)
    ProductStatus status;
    String sku;
    String brand;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Categories category;

    @Version
    Long version;

	public Product() {
    }

    public Product(String name, String description, Double price,  Long sellerId,
            ProductStatus status, String sku, String brand, LocalDateTime createdAt, LocalDateTime updatedAt,
            Categories category) {
        this.name = name;
        this.description = description;
        this.price = price;
        // this.stockQuantity = stockQuantity;
        this.sellerId = sellerId;
        this.status = status;
        this.sku = sku;
        this.brand = brand;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
    }

    @PrePersist
    public void createdDate(){
        LocalDateTime now=LocalDateTime.now();
        this.createdAt=now;
        this.updatedAt=now;
    }

    @PreUpdate
    public void updateDate(){
        this.updatedAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    // public Integer getStockQuantity() {
    //     return stockQuantity;
    // }
    // public void setStockQuantity(Integer stockQuantity) {
    //     this.stockQuantity = stockQuantity;
    // }
    public Long getSellerId() {
        return sellerId;
    }
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
    public ProductStatus getStatus() {
        return status;
    }
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdadedAt() {
        return updatedAt;
    }
    public void setUpdadedAt(LocalDateTime updadedAt) {
        this.updatedAt = updadedAt;
    }
    public Categories getCategory() {
        return category;
    }
    public void setCategory(Categories category) {
        this.category = category;
    }
    public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
}
