package com.project.ORDER.Controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ORDER.DTO.CartDTO;
import com.project.ORDER.Entities.Cart;
import com.project.ORDER.Entities.CartItems;
import com.project.ORDER.ResponseDTO.PageCartResponse;
import com.project.ORDER.Service.OrderService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/order/cart")
public class CartController {

    final OrderService orderService;

    CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @PostMapping("/addToCart")
    public String addCart(@RequestBody @Valid  CartDTO cartDTO) {
        return orderService.addToCart(cartDTO);
    }
    
    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @GetMapping("/getMyCart")
    public Cart myCart() {
        return orderService.getMycart();
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @PatchMapping("/updateQuantity")
    public String updateQuantity(@RequestBody CartDTO cartDTO){
        return orderService.updateQuantity(cartDTO);
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @DeleteMapping("/deleteCart")
    public String deleteCart(){
        return orderService.deletecart();
    }

    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @DeleteMapping("/deleteItemInCart/{productId}")
    public String deleteItemInCart(@PathVariable Long productId){
        return orderService.deleteItemIncart(productId);
    }    
    
    @PreAuthorize("hasAnyRole('USER','PRODUCT_OWNER')")
    @GetMapping("/getMyCartItems")
    public PageCartResponse getMethodName(Pageable pageable) {
        return orderService.myCartItems(pageable);
    }
    
    
}
