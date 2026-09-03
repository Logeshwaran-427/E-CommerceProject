package com.project.PAYMENT.Client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OrderClient {
    final WebClient webClient;

    public OrderClient(WebClient webClient){
        this.webClient=webClient;
    }

    final String ORDERURL="http://localhost:8084/order";

    public void confirmOrder(Long orderId, String token, String correlationId){
        webClient.patch().uri(ORDERURL+"/confirmOrder/"+orderId).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).retrieve().
        bodyToMono(Void.class).block();    
    }
}
