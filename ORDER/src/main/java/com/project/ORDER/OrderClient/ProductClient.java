package com.project.ORDER.OrderClient;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.ORDER.DTO.ProductIdsRequestDTO;
import com.project.ORDER.DTO.ProductResponseDTO;
import com.project.ORDER.DTO.ProductSellerRequestDTO;
import com.project.ORDER.DTO.ProductSellerResponseDTO;
import com.project.ORDER.ExceptionHandling.ResourceNotFoundException;

@Component
public class ProductClient {

    final WebClient webClient;

    public ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    final String PRODUCTURL="http://localhost:8082/product";
    final String INVENTORYURL="http://localhost:8083/inventory";

    public ProductResponseDTO getProduct(Long id,String token,String correlationId){
        try{
        ProductResponseDTO product=webClient.get().uri(PRODUCTURL+"/getProduct/"+id).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(ProductResponseDTO.class).block();
        return product;
        }
        catch(Exception ex){
            throw new ResourceNotFoundException("Invalid productId");
        }
    }

    public List<ProductResponseDTO> getCheckoutProducts(ProductIdsRequestDTO productIds,String token,String correlationId){
    
        List<ProductResponseDTO> products=webClient.post().uri(PRODUCTURL+"/getByProductIds").bodyValue(productIds).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToFlux(ProductResponseDTO.class).collectList().block();
        return products;

    }



    
}
