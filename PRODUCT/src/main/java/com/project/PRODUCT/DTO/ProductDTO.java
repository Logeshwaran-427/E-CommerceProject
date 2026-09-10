package com.project.PRODUCT.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDTO {

    @NotBlank(message = "Product name is required")
    String name;
    @NotBlank(message = "Description is required")
    @Size(min=6, message = "Minimum six characters is required")
    String description;
    @NotNull (message = "Price field is required")
    @Positive(message = "Price must be greater than zero")
    Double price;
    @NotBlank(message = "Brand field is required")
    String brand;
    @NotNull (message = "Category Id is required")
    @Positive (message = "ID should be positive")
    Long categoryId;

    public ProductDTO(String name, String description, Double price,  String brand, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.categoryId = categoryId;
    }

    public ProductDTO() {
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
    
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


}
