package com.project.ORDER.ResponseDTO;

import java.util.List;

public class PageCartResponse {
    List<CartResponseDTO> cartItems;
    int currentPage;
    int totalPages;
    long totalElements;
}
