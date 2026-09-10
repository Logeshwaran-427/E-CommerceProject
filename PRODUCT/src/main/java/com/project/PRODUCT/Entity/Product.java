package com.project.PRODUCT.Entity;

import java.time.LocalDateTime;

import com.project.PRODUCT.Enums.ProductStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

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
    @NotBlank(message = "Product name is required")
    String name;
    @NotBlank(message = "Description is required")
    @Size(min=6, message = "Minimum six characters is required")
    String description;
    @NotNull(message = "Price field is required")
    @Positive(message = "Price must be greater than zero")
    Double price;
    Long sellerId;
    @Enumerated(EnumType.STRING)
    ProductStatus status;
    @Column(unique = true, nullable = false)
    String sku;
    @NotBlank(message = "Brand field is required")
    String brand;
    Long createdBy;
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
            Categories category,Long createdBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
        this.status = status;
        this.sku = sku;
        this.brand = brand;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.createdBy=createdBy;
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
        public Long getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
