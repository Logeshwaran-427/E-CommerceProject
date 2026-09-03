package com.project.INVENTORY.Client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.INVENTORY.DTO.ProductClient;
import com.project.INVENTORY.DTO.ProductPageResponseDTO;
import com.project.INVENTORY.ExceptionHandling.ResourceNotFoundException;

@Component
public class InventoryClient {

    
    final WebClient webClient;

    public InventoryClient(WebClient webClient) {
        this.webClient = webClient;
    }

    final String PRODUCTURL="http://localhost:8082/product";
    
    public ProductClient findProduct(Long id,String token,String correlationId){
        try{
        ProductClient productClient=webClient.get().uri(PRODUCTURL+"/getProduct/"+id).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().
        bodyToMono(ProductClient.class).block();
        return productClient;
        }
        catch(Exception ex){
            throw new ResourceNotFoundException("Invalid Product id");
        }

    }

    public List<ProductClient> currentSellerProducts(String token,int page, String correlationId){
        ProductPageResponseDTO productResonse=webClient.get().uri(PRODUCTURL + "/getMyProducts",uriBuilder->uriBuilder.queryParam("page",page).build()).
                                        header("Authorization", token).header("X-Correlation-ID", correlationId).
                                        retrieve().bodyToMono(ProductPageResponseDTO.class).block();
        List<ProductClient> products=productResonse.getContent();
        return products;
    }



}
