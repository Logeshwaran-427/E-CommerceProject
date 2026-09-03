package com.project.PAYMENT.Service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.PAYMENT.Client.OrderClient;
import com.project.PAYMENT.DTO.PaymentRequestDTO;
import com.project.PAYMENT.DTO.UserContext;
import com.project.PAYMENT.Entity.Payment;
import com.project.PAYMENT.Enum.PaymentMethod;
import com.project.PAYMENT.Enum.PaymentStatus;
import com.project.PAYMENT.Exceptions.AuthorizationException;
import com.project.PAYMENT.Exceptions.BadRequestException;
import com.project.PAYMENT.Exceptions.ResourceNotFoundException;
import com.project.PAYMENT.Repository.PaymentRepo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    final PaymentRepo paymentRepo;
    final OrderClient orderClient;
    final HttpServletRequest request;

    Logger logger=LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(PaymentRepo paymentRepo,OrderClient orderClient,HttpServletRequest request){
        this.paymentRepo=paymentRepo;
        this.orderClient=orderClient;
        this.request=request;
    }

    public String createPayment(PaymentRequestDTO paymentRequestDTO){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId =userContext.getUserId();
        logger.info("Payment initiation requested for orderId={} by userId={}",paymentRequestDTO.getOrderId(), userId);

        if(paymentRepo.existsByOrderId(userId)){
            throw new BadRequestException("Payment already exists for this order");
        }


        Payment payment=new Payment();
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setOrderId(paymentRequestDTO.getOrderId());
        payment.setUserId(userId);
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setTransactionId(UUID.randomUUID().toString().substring(0,8));
        payment.setStatus(PaymentStatus.PENDING);
        paymentRepo.save(payment);
        logger.info("Payment record created successfully. orderId={}, transactionId={}, amount={}",payment.getOrderId(),payment.getTransactionId(),payment.getAmount());

        return "Payment initiated";

    }

    @Transactional
    public String confirmPayment(Long orderId) {

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId =userContext.getUserId();
        logger.info("Payment confirmation requested for orderId={} by userId={}",orderId, userId);
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        Payment payment=paymentRepo.findByOrderId(orderId).orElseThrow(()-> new ResourceNotFoundException("Payment does nor exist for this order"));

        if(!payment.getUserId().equals(userId)){
            throw new AuthorizationException("You are not authorized to access this order id");
        }

        if(payment.getStatus()!=PaymentStatus.PENDING){
            throw new BadRequestException("Payment already completed");
        }
                
        payment.setStatus(PaymentStatus.COMPLETED);
        logger.info("Payment completed successfully for orderId={}, transactionId={}",payment.getOrderId(),payment.getTransactionId());

        paymentRepo.save(payment);
        logger.info("Sending payment confirmation to Order Service for orderId={}",orderId);
        orderClient.confirmOrder(orderId, token,corrId);
        logger.info("Order Service confirmed orderId={}", orderId);
        
        return "Payment Successful";

    }

    public void cancelPayment(Long orderId){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId =userContext.getUserId();
        logger.info("Payment cancellation requested for orderId={} by userId={}",orderId, userId);
        Payment payment=paymentRepo.findByOrderId(orderId).orElseThrow(()->new ResourceNotFoundException("Payment record not found"));
        if(!payment.getUserId().equals(userId)){
            throw new AuthorizationException("You are not authorized to access this order id");
        }
        if(payment.getStatus()==PaymentStatus.FAILED || payment.getStatus()==PaymentStatus.REFUNDED){
            throw new BadRequestException("Payment already cancelled");
        }
        if(payment.getStatus() == PaymentStatus.COMPLETED){
            payment.setStatus(PaymentStatus.REFUNDED);
            logger.info("Payment status changed to {} for orderId={}",payment.getStatus(),orderId);
        }
        else if(payment.getStatus() == PaymentStatus.PENDING){
            payment.setStatus(PaymentStatus.FAILED);
            logger.info("Payment status changed to {} for orderId={}",payment.getStatus(),orderId);
        }
        paymentRepo.save(payment);
    }

    public void cancelPaymentForItem(Long orderId,Double price){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId =userContext.getUserId();
        logger.info("Partial refund requested for orderId={} amount={} by userId={}",orderId,price,userId);
        Payment payment=paymentRepo.findByOrderId(orderId).orElseThrow(()->new ResourceNotFoundException("Payment record not found"));
        if(!payment.getUserId().equals(userId)){
            throw new AuthorizationException("You are not authorized to access this order id");
        }
        if(payment.getStatus()==PaymentStatus.FAILED || payment.getStatus()==PaymentStatus.REFUNDED){
            throw new BadRequestException("Payment already cancelled");
        }
        payment.setAmount(payment.getAmount()-price);
        
        paymentRepo.save(payment);
        logger.info("Payment amount updated for orderId={}. Remaining payable amount={}",orderId,payment.getAmount());

        

    }

    
    
}
