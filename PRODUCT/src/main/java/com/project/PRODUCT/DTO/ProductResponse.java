package com.project.PRODUCT.DTO;

import com.project.PRODUCT.Enums.ProductStatus;

public class ProductResponse {
            Long id;
            String name;
            String description;
            Double price;
            String brand;
            String category;
            ProductStatus status;



            public ProductResponse(Long id, String name, String description, Double price, String brand,
                    String category,ProductStatus status) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.price = price;
                this.brand = brand;
                this.category = category;
                this.status=status;
            }

            public ProductResponse() {
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
            public String getBrand() {
                return brand;
            }
            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getCategory() {
                return category;
            }
            public void setCategory(String category) {
                this.category = category;
            }

            public ProductStatus getStatus() {
                return status;
            }
            public void setStatus(ProductStatus status) {
                this.status = status;
            }

     

}
