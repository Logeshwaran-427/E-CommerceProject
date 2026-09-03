package com.project.ORDER.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ORDER.Entities.Cart;
import com.project.ORDER.Entities.CartItems;
import com.project.ORDER.Service.OrderService;



@RestController
@RequestMapping("/order/cart")
public class CartController {

    final OrderService orderService;

    CartController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/addTocart/{productId}")
    public String addCart(@PathVariable Long productId,@RequestParam Integer quantity) {
        return orderService.addToCart(productId, quantity);
    }
    
    @GetMapping("/getMycart")
    public Cart myCart() {
        return orderService.getMycart();
    }

    @PatchMapping("/updateQuantity/{productId}")
    public String updateQuantity(@PathVariable Long productId,@RequestParam Integer quantiyy){
        return orderService.updateQuantity(productId,quantiyy);
    }

    @DeleteMapping("/deleteCart")
    public String deleteCart(){
        return orderService.deletecart();
    }

    @DeleteMapping("/deleteItemInCart/{productId}")
    public String deleteItemInCart(@PathVariable Long productId){
        return orderService.deleteItemIncart(productId);
    }    
    
    @GetMapping("/getMyCartItems")
    public List<CartItems> getMethodName() {
        return orderService.myCartItems();
    }
    
    
}
