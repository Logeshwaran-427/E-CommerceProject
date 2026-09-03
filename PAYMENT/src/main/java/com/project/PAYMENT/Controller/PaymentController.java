package com.project.PAYMENT.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.PAYMENT.DTO.PaymentRequestDTO;
import com.project.PAYMENT.Service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }
    
    @PostMapping("/createPayment")
    public String createPayment(@RequestBody PaymentRequestDTO paymentRequestDTO){
        return paymentService.createPayment(paymentRequestDTO);
    }

    @PatchMapping("/confirmPayment/{id}")
    public String confirmPayment(@PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }

    @PatchMapping("/cancelOrder/{orderId}")
    public void cancelPayment(@PathVariable Long orderId){
        paymentService.cancelPayment(orderId);
    }

    @PatchMapping("/cancelOrderForItem{orderId}")
    public void cancelpaymetItem(@PathVariable Long orderId, @RequestParam Double price){
        paymentService.cancelPaymentForItem(orderId,price);
    }
}
