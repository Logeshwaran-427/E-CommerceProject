package com.project.PRODUCT.Client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductClient {

    final WebClient webClient;

    public ProductClient(WebClient webClient){
        this.webClient=webClient;
    }
    
    final static String INVENTORYURL ="http://localhost:8083/inventory";

    public void deleteInventory(Long id, String token,String correlationId){
        webClient.patch().uri(INVENTORYURL+"/deleteInventory/"+id).
        header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }
}
