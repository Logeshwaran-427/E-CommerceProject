package com.project.ORDER.OrderClient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.ORDER.DTO.PaymentRequesDTO;

@Component
public class PaymentClient {
    
    final WebClient webClient;
    public PaymentClient (WebClient webClient){
        this.webClient=webClient;
    }

    final String PAYMENTURL="http://localhost:8085/payment";

    public void createPayment(PaymentRequesDTO paymentRequesDTO,String token,String correlationId){

        webClient.post().uri(PAYMENTURL+"/createPayment").bodyValue(paymentRequesDTO).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }

    public void cancelPayment(Long orderId, String token,String correlationId){

        webClient.patch().uri(PAYMENTURL+"/cancelOrder/"+orderId).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }

    public void cancelPaymentForItem(Long orderId, Double price,String token,String correlationId){
        webClient.patch().uri(PAYMENTURL+"/cancelOrderForItem/"+orderId+"?price="+price).header("Authorization", token).header("Authorization", token).header("X-Correlation-ID", correlationId).
        retrieve().bodyToMono(Void.class).block();
    }




}
