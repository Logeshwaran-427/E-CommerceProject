package com.project.PRODUCT.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDTO {
    
    @NotBlank (message = "Category name is required")
    String name;
    @NotBlank (message = "Description is required")
    @Size (min = 3, message = "Length should be greater than 3")
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
