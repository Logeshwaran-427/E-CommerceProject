package com.project.PRODUCT.DTO;

public class ProductDTO {

    String name;
    String description;
    Double price;
    // int stockQuantity;
    String brand;
    Long categoryId;

    public ProductDTO(String name, String description, Double price,  String brand, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        // this.stockQuantity = stockQuantity;
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
    // public int getStockQuantity() {
    //     return stockQuantity;
    // }
    // public void setStockQuantity(int stockQuantity) {
    //     this.stockQuantity = stockQuantity;
    // }
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
