package com.project.ORDER.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ORDER.DTO.CheckoutCartRequest;
import com.project.ORDER.DTO.PlaceDirectOrderRequest;
import com.project.ORDER.DTO.PlaceOrderRequestDTO;
import com.project.ORDER.Entities.Address;
import com.project.ORDER.Entities.Order1;
import com.project.ORDER.Entities.OrderItem;
import com.project.ORDER.Enums.OrderItemStatus;
import com.project.ORDER.ResponseDTO.SellerDashboardDTO;
import com.project.ORDER.ResponseDTO.SellerOrderResponse;
import com.project.ORDER.Service.OrderService;


@RestController
@RequestMapping("/order")
public class OrderController {

    final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO){
        return orderService.placeOrder(placeOrderRequestDTO);
    }

    @PatchMapping("/confirmOrder/{id}")
    public void confirmOrder(@PathVariable Long id){
         orderService.confirmOrder(id);
    }
    
    @PostMapping("/checkOutCart")
    public String checkOutCart(@RequestBody CheckoutCartRequest checkoutCartRequest) {    
        return orderService.checkoutCart(checkoutCartRequest);
    }
    
    @PostMapping("/buyNow")
    public String directlyPlaceOrder(@RequestBody PlaceDirectOrderRequest placeDirectOrderRequest) {
        //TODO: process POST request
        
        return orderService.directOrder(placeDirectOrderRequest);
    }
    
    @GetMapping("/getMyOrders")
    public List<Order1> getMyOrders() {
        return orderService.getMyOrders();
    }
    
    @GetMapping("/getOrderitems/{orderId}")
    public List<OrderItem> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PatchMapping("/shipProducts/{orderId}")
    public String shippingProducts(@PathVariable Long orderId){
        return orderService.shipProduct(orderId);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/deliverProducts/{orderId}")
    public String deliverProducts(@PathVariable Long orderId) {
        return orderService.deliverProduct(orderId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER','PRODUCT_OWNER')")
    @PostMapping("/address")
    public String addAddress(@RequestBody Address address) {  
        return orderService.addAddress(address);
    }

    @PatchMapping("/cancelOrder/{orderId}")
    public String cancelOrder(@PathVariable Long orderId){
        return orderService.cancelOrder(orderId);
    }
    
    @GetMapping("/seller/myProductOrders")
    public List<SellerOrderResponse> myProductOrders() {
        return orderService.sellerOrders();
    }

    @GetMapping("/seller/status/{itemStatus}")
    public List<SellerOrderResponse> myOrderStatus(@PathVariable OrderItemStatus itemStatus) {
        return orderService.getOrderItemsByStatus(itemStatus);
    }

    @GetMapping("/seller/dashboard")
    public SellerDashboardDTO dashboard() {
        return orderService.sellerdahboard();
    }

    @PatchMapping("/cancelItem/{orderId}/{orderItemId}")
    public String cancelItem(@PathVariable Long orderId, @PathVariable Long orderItemId){
        return orderService.cancelByOrderItems(orderId, orderItemId);
    }
    
    
        
}



// ⭐ Deliver Order

//         ↓

// ⭐ Cancel Order

//         ↓

// Seller Dashboard

//         ↓

// Address Module

//         ↓

// Notification Service (Kafka)

//         ↓

// Review Service

//         ↓

// Performance Optimizations
