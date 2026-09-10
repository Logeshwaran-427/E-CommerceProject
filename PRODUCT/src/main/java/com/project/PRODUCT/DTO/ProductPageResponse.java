package com.project.PRODUCT.DTO;

import java.util.List;

public class ProductPageResponse {

    List<ProductResponse> products;
    int currentPage;
    int totalPages;
    long totalElements;

    public ProductPageResponse(List<ProductResponse> products, int currentPage, int totalPages, long totalElements) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    public ProductPageResponse() {
    }
    
    public List<ProductResponse> getProducts() {
        return products;
    }
    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public long getTotalElements() {
        return totalElements;
    }
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
