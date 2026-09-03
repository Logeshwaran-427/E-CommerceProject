package com.project.ORDER.OrderClient;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.ORDER.DTO.InventoryRequestDTO;

@Component
public class InventoryClient {

    final WebClient webClient;
    final ProductClient productClient;

    public InventoryClient(WebClient webClient,ProductClient productClient) {
        this.webClient = webClient;
        this.productClient=productClient;
    }

    final String INVENTORYURL="http://localhost:8083/inventory";

    public boolean checkInventory(Long productId, Integer quantity, String token,String correlationId){
        boolean isAvailable=webClient.get().uri(INVENTORYURL+"/stockStatus/"+productId+"?stock="+quantity).
                            header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).retrieve().bodyToMono(Boolean.class).block();
        
        return isAvailable;
        
    }

    public void updateReservedStock(Long productId, Integer quantity, String token,String correlationId){
        webClient.patch().uri(INVENTORYURL+"/updateReservedStock/"+productId+"?stock="+quantity).
        header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).retrieve().bodyToMono(Void.class).block();

    }

    public boolean checkoutCartInventory(List<InventoryRequestDTO> inventoryRequestDTO, String token,String correlationId){
        boolean isAvailable=webClient.post().uri(INVENTORYURL+"/checkMultipleInventory").bodyValue(inventoryRequestDTO).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Boolean.class).block();
        return isAvailable;
    }

    public void updateReservedStockForMultipleProducts(List<InventoryRequestDTO> inventoryRequestDTOs,String token,String correlationId){
        webClient.patch().uri(INVENTORYURL+"/updateMultipleReservedStocks").bodyValue(inventoryRequestDTOs).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }

    public void releaseReservedStock(Long productId,Integer quantity, String token,String correlationId){
        webClient.patch().uri(INVENTORYURL+"/releaseReservedStock/"+productId+"?quantity="+quantity).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }

    public void cancelReservedStock(Long productId,Integer quantity, String token,String correlationId){
        webClient.patch().uri(INVENTORYURL+"/cancelReservedStock/"+productId+"?quantity="+quantity).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }
    
}
