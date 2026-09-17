package com.project.ORDER.ResponseDTO;

import java.util.List;

public class PageCartResponse {
    List<CartResponseDTO> cartItems;
    int currentPage;
    int totalPages;
    long totalElements;
    public PageCartResponse(List<CartResponseDTO> cartItems, int currentPage, int totalPages, long totalElements) {
        this.cartItems = cartItems;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
    public PageCartResponse() {
    }
    public List<CartResponseDTO> getCartItems() {
        return cartItems;
    }
    public void setCartItems(List<CartResponseDTO> cartItems) {
        this.cartItems = cartItems;
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
