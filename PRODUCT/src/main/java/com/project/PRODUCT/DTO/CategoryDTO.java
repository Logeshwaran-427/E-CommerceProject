package com.project.PRODUCT.DTO;

public class CategoryDTO {
    
    String name;
    String description;
    public CategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public CategoryDTO() {
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
}
