package com.project.INVENTORY.DTO;

import java.util.List;

public class ProductPageResponseDTO {
    List<ProductClient> content;

    public ProductPageResponseDTO(List<ProductClient> content) {
        this.content = content;
    }
    public ProductPageResponseDTO() {
    }
    public List<ProductClient> getContent() {
        return content;
    }
    public void setContent(List<ProductClient> content) {
        this.content = content;
    }
}
