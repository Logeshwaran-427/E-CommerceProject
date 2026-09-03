package com.project.ORDER.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ORDER.DTO.CheckoutCartRequest;
import com.project.ORDER.DTO.InventoryRequestDTO;
import com.project.ORDER.DTO.OrderItemStatusNotification;
import com.project.ORDER.DTO.OrderStatusNotification;
import com.project.ORDER.DTO.PaymentRequesDTO;
import com.project.ORDER.DTO.PlaceDirectOrderRequest;
import com.project.ORDER.DTO.PlaceOrderRequestDTO;
import com.project.ORDER.DTO.ProductIdsRequestDTO;
import com.project.ORDER.DTO.ProductResponseDTO;
import com.project.ORDER.DTO.UserContext;
import com.project.ORDER.Entities.Address;
import com.project.ORDER.Entities.Cart;
import com.project.ORDER.Entities.CartItems;
import com.project.ORDER.Entities.Order1;
import com.project.ORDER.Entities.OrderItem;
import com.project.ORDER.Enums.NotificationType;
import com.project.ORDER.Enums.OrderItemStatus;
import com.project.ORDER.Enums.OrderStatus;
import com.project.ORDER.Enums.OrderType;
import com.project.ORDER.Enums.PaymentStatus;
import com.project.ORDER.Enums.ProductStatus;
import com.project.ORDER.ExceptionHandling.AuthorizationException;
import com.project.ORDER.ExceptionHandling.BadRequestException;
import com.project.ORDER.ExceptionHandling.ResourceNotFoundException;
import com.project.ORDER.OrderClient.InventoryClient;
import com.project.ORDER.OrderClient.PaymentClient;
import com.project.ORDER.OrderClient.ProductClient;
import com.project.ORDER.Repository.AddressRepo;
import com.project.ORDER.Repository.CartItemRepo;
import com.project.ORDER.Repository.CartRepository;
import com.project.ORDER.Repository.OrderItemRepo;
import com.project.ORDER.Repository.OrderRepo;
import com.project.ORDER.ResponseDTO.SellerDashboardDTO;
import com.project.ORDER.ResponseDTO.SellerOrderResponse;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class OrderService {

    final OrderRepo orderRepo;
    final CartRepository cartRepo;
    final HttpServletRequest request;
    final ProductClient productClient;
    final CartItemRepo cartItemRepo;
    final InventoryClient inventoryClient;
    final OrderItemRepo orderItemRepo;
    final PaymentClient paymentClient;
    final AddressRepo addressRepo;
    final KafkaTemplate<String,Object> kafkaTemplate;
    
    

    public OrderService(KafkaTemplate<String,Object> kafkaTemplate,InventoryClient inventoryClient,AddressRepo addressRepo,PaymentClient paymentClient,OrderItemRepo orderItemRepo,OrderRepo orderRepo,CartRepository cartRepo,HttpServletRequest request,ProductClient productClient,CartItemRepo cartItemRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo=cartRepo;
        this.request=request;
        this.productClient=productClient;
        this.cartItemRepo=cartItemRepo;
        this.inventoryClient=inventoryClient;
        this.orderItemRepo=orderItemRepo;
        this.paymentClient=paymentClient;
        this.addressRepo=addressRepo;
        this.kafkaTemplate=kafkaTemplate;
    }

    Logger logger=LoggerFactory.getLogger(OrderService.class);


    public String addAddress(Address address){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Adding address for userId={}", userId);
        if(address.getFullName()==null || address.getAddress()==null || address.getAddressType()==null || address.getPostalCode()==null || address.getPhoneNumber()==null){
            throw new BadRequestException("Please fill all the fields");
        }
        address.setUserId(userId);
        addressRepo.save(address);
        logger.info("Address saved successfully for userId={}", userId);
        return "Address saved successfully";
    }


    @Transactional
    public String addToCart(Long productId,Integer quantity){

        if(productId==null || quantity==null){
            throw new BadRequestException("Please complete the required field");
        }

        if(quantity<=0){
            throw new BadRequestException("Qunatity should be positive");
        }
        if(quantity>100){
            throw new BadRequestException("Add quantity less than 100");
        }

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        logger.info("User {} adding product {} to cart", userId, productId);

        ProductResponseDTO product=productClient.getProduct(productId, token,corrId);

        if(product.getStatus()!=ProductStatus.ACTIVE){
            throw new BadRequestException("Product is inactive");
        }

        Cart cart=cartRepo.findByUserId(userId).orElseGet(()->{Cart cart1=new Cart();
                                                                cart1.setUserId(userId);
                                                                return cartRepo.save(cart1);
                                                                });
        
        Optional<CartItems> cartItem=cartItemRepo.findByCartAndProductId(cart,productId);
        if(cartItem.isPresent()){
            CartItems cartItem1=cartItem.get();
            Integer oldQuantity=cartItem1.getQuantity();
            Integer newQuantity=quantity+oldQuantity;
            if(newQuantity>100){
                throw new BadRequestException("Add quantity less than 100. Beacause you already having "+oldQuantity+" for this product in cart");
            }
            cartItem1.setQuantity(oldQuantity+quantity);
            cartItem1.setPrice(product.getPrice());
            cartItemRepo.save(cartItem1);
            logger.info("Updated quantity of product {} in cart", productId);
            return "Cart item added";
        }
        
        
        CartItems cartItems=new CartItems();
        cartItems.setCart(cart);
        cartItems.setProductId(product.getId());
        cartItems.setQuantity(quantity);
        cartItems.setPrice(product.getPrice());
        cartItemRepo.save(cartItems);
        logger.info("Product {} added to cart", productId);
        return "Product added to cart successfully";

    }

    public Cart getMycart(){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Fetching cart for userId={}", userId);
        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found. Please add any items to view your cart"));
        return cart;
    }

    public String updateQuantity(Long productId,Integer quantity){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Updating cart quantity for product {}", productId);
        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));

        List<CartItems> cartItems=cart.getCartItems();
        CartItems cartItems2=null;
        for(CartItems items:cartItems){
            if(items.getProductId().equals(productId)){
                cartItems2=items;
            }
        }
        
        if(cartItems2==null){
            throw new ResourceNotFoundException("Product not found in the cart");
        }
        cartItems2.setQuantity(quantity);
        cartItemRepo.save(cartItems2);
        logger.info("Quantity updated successfully for product {}", productId);
        return "Quantity updated successfully";
    }

    public String deleteItemIncart(Long productId){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Removing product {} from cart", productId);
        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));

        List<CartItems> cartItems=cart.getCartItems();

        CartItems cartItems2=null;
        for(CartItems items:cartItems){
            if(items.getProductId().equals(productId)){
                cartItems2=items;
            }
        }
        
        if(cartItems2==null){
            throw new ResourceNotFoundException("Product not found in the cart");
        }

        cart.getCartItems().remove(cartItems2);
        cartItemRepo.save(cartItems2);
        logger.info("Product {} removed from cart", productId);
        return "Item successfully deleted";
    }

    public String deletecart(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Clearing cart for userId={}", userId);
        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));
        cartRepo.delete(cart);
        logger.info("Cart deleted successfully");
        return "Cart deleted successfully";
    }

    public List<CartItems> myCartItems(){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));
        return cartItemRepo.findAllByCart(cart);        
    }

    @Transactional
    public String placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO){

        if(placeOrderRequestDTO.getPaymentMethod()==null){
            throw new BadRequestException("please select the payment method");
        }

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        logger.info("Buy Now initiated by user {}", userId);
        Long productId=placeOrderRequestDTO.getProductId();
        Long addressId=placeOrderRequestDTO.getAddressId();
        Address address=addressRepo.findById(addressId).orElseThrow(()->  new ResourceNotFoundException("Address not available"));
        if(!address.getUserId().equals(userId)){
            throw new BadRequestException("Address not belongs to the user");
        }

        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
        CartItems cartItems=cartItemRepo.findByCartAndProductId(cart, productId).orElseThrow(()->new ResourceNotFoundException("requested product not found in the cart"));
        Integer quantity= cartItems.getQuantity();

        if(productId==null){
            throw new BadRequestException("Please complete the required field");
        }

        if(quantity<=0){
            throw new BadRequestException("Qunatity should be positive");
        }
        if(quantity>100){
            throw new BadRequestException("Add quantity less than 100");
        }


        ProductResponseDTO product=productClient.getProduct(productId, token,corrId);

        
        inventoryClient.checkInventory(productId, quantity, token,corrId);
        logger.info("Inventory verified for product {}", productId);

        Order1 order1=new Order1();
        order1.setUserId(userId);
        order1.setAddressId(addressId);
        order1.setOrderType(OrderType.CART);
        order1.setOrderStatus(OrderStatus.PAYMENT_PENDING);
        order1.setPaymentStatus(PaymentStatus.PENDING);
        order1.setTotalAmount(product.getPrice()*quantity);
        

        OrderItem orderItem=new OrderItem();
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setProductId(productId);
        orderItem.setSellerId(product.getSellerId());
        orderItem.setOrderItemStatus(OrderItemStatus.PAYMENT_PENDING);
        orderItem.setSubTotal(product.getPrice()*quantity);

        order1.addOrderItem(orderItem);
        orderRepo.save(order1);
        logger.info("Order {} created", order1.getId());
        
        inventoryClient.updateReservedStock(productId, quantity, token,corrId);
        logger.info("Reserved stock updated for product {}", productId);

        PaymentRequesDTO paymentRequesDTO=new PaymentRequesDTO();
        paymentRequesDTO.setAmount(order1.getTotalAmount());
        paymentRequesDTO.setOrderId(order1.getId());
        paymentRequesDTO.setPaymentMethod(placeOrderRequestDTO.getPaymentMethod());

        paymentClient.createPayment(paymentRequesDTO, token,corrId);
        logger.info("Payment initiated for order {}", order1.getId());
        
        
        return "Your order created successfully.";
    }

    public void confirmOrder(Long orderId){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        // Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));
        
        Order1 order=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order id not found"));
        logger.info("Confirming order {}", orderId);
        if(!order.getUserId().equals(userId)){
            throw new BadRequestException("Please enter the correct orderId");
        }
            order.setOrderStatus(OrderStatus.CONFIRMED);
            logger.info("Order {} confirmed", orderId);
            order.setPaymentStatus(PaymentStatus.COMPLETED);
            logger.info("Payment completed for order {}", orderId);
            orderRepo.save(order);
        
            List<OrderItem> orderItems1=order.getOrderItems();
            for(OrderItem item:orderItems1){
                item.setOrderItemStatus(OrderItemStatus.CONFIRMED);
            }

            OrderStatusNotification notification=new OrderStatusNotification();
            notification.setUserId(userId);
            notification.setOrderId(orderId);
            notification.setType(NotificationType.ORDER_CONFIRMED);
            notification.setMessage("Your order has been placed");

            kafkaTemplate.send("order-confirmed",notification);
        


        if(order.getOrderType()==OrderType.CART){
            Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));
            List<OrderItem> orderItems=order.getOrderItems();
            List<Long> productIds=new ArrayList<>();
            for(OrderItem item:orderItems){
                productIds.add(item.getProductId());
            }
            List<CartItems> cartItems=cartItemRepo.findAllByCartAndProductIdIn(cart,productIds);
            cart.getCartItems().removeAll(cartItems);
            cartRepo.save(cart);
            logger.info("Purchased items removed from cart");
        }
    }


    @Transactional
    public String checkoutCart(CheckoutCartRequest checkoutCartRequest){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        logger.info("Checkout started for user {}", userId);
        Long addressId=checkoutCartRequest.getAddressId();
        Address address=addressRepo.findById(addressId).orElseThrow(()->  new ResourceNotFoundException("Address not available"));
        if(!address.getUserId().equals(userId)){
            throw new BadRequestException("Address not belongs to the user");
        }

        Cart cart=cartRepo.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Cart not found for the user"));

        List<CartItems> cartItems=cartItemRepo.findAllByCart(cart);

        if(cartItems.isEmpty()){
            throw new BadRequestException("Cart is empty.");
        }
        logger.info("{} products loaded from cart", cartItems.size());
        List<Long> productIds=new ArrayList<>();
        List<InventoryRequestDTO> inventories=new ArrayList<>();
        for(CartItems id:cartItems){
            productIds.add(id.getProductId());

            InventoryRequestDTO  inventoryRequestDTO=new InventoryRequestDTO();
            inventoryRequestDTO.setProductId(id.getProductId());
            inventoryRequestDTO.setStock(id.getQuantity());
            inventories.add(inventoryRequestDTO);
        }
        ProductIdsRequestDTO productIdsRequestDTO=new ProductIdsRequestDTO();
        productIdsRequestDTO.setProductIds(productIds);
        List<ProductResponseDTO> products=productClient.getCheckoutProducts(productIdsRequestDTO,token,corrId );

        inventoryClient.checkoutCartInventory(inventories,token,corrId);
        logger.info("Inventory verified for checkout");

        Map<Long,CartItems> cartItemMap=cartItems.stream().collect(Collectors.toMap(items->items.getProductId(),item->item));

        Order1 order=new Order1();
            order.setOrderStatus(OrderStatus.PAYMENT_PENDING);
            order.setOrderType(OrderType.CART);
            order.setPaymentStatus(PaymentStatus.PENDING);
            order.setUserId(userId);
            order.setAddressId(addressId);

        Double totalAmount=0.0;

        for(ProductResponseDTO i: products){
            CartItems cartItem=cartItemMap.get(i.getId());
            Integer quantity= cartItem.getQuantity();
            if(quantity<=0){
                throw new BadRequestException("Qunatity should be positive");
            }
            if(quantity>100){
                throw new BadRequestException("Add quantity less than 100");
            }


            Double totalProductPrice=i.getPrice()*quantity;
            totalAmount=totalAmount+totalProductPrice;

            OrderItem orderItem=new OrderItem();
            orderItem.setSubTotal(totalProductPrice);
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(i.getPrice());
            orderItem.setSellerId(i.getSellerId());
            orderItem.setOrderItemStatus(OrderItemStatus.PAYMENT_PENDING);
            orderItem.setOrder(order);
            order.addOrderItem(orderItem);

        }

        order.setTotalAmount(totalAmount);
        orderRepo.save(order);
        logger.info("Checkout order {} created", order.getId());

        inventoryClient.updateReservedStockForMultipleProducts(inventories, token,corrId);
        
        PaymentRequesDTO paymentRequesDTO=new PaymentRequesDTO();
        paymentRequesDTO.setAmount(order.getTotalAmount());
        paymentRequesDTO.setOrderId(order.getId());
        
        paymentRequesDTO.setPaymentMethod(checkoutCartRequest.getPaymentMethod());

        paymentClient.createPayment(paymentRequesDTO, token,corrId);
        logger.info("Payment initiated for order {}", order.getId());

        return "Order created successfully";    

    }

    @Transactional
    public String directOrder(PlaceDirectOrderRequest placeDirectOrderRequest){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        logger.info("Buy Now initiated by user {}", userId);
        Long addressId=placeDirectOrderRequest.getAddressId();
        Address address=addressRepo.findById(addressId).orElseThrow(()->  new ResourceNotFoundException("Address not available"));
        if(!address.getUserId().equals(userId)){
            throw new BadRequestException("Address not belongs to the user");
        }

        if (placeDirectOrderRequest.getProductId() == null) {
            throw new BadRequestException("Product id is required");
        }
        
        if (placeDirectOrderRequest.getQuantity() == null) {
            throw new BadRequestException("Quantity is required");
        }
        
        if (placeDirectOrderRequest.getQuantity() <= 0) {
            throw new BadRequestException("Quantity should be positive");
        }
        
        if (placeDirectOrderRequest.getQuantity() > 100) {
            throw new BadRequestException("Maximum quantity is 100");
        }

        ProductResponseDTO product=productClient.getProduct(placeDirectOrderRequest.getProductId(), token,corrId);
        inventoryClient.checkInventory(placeDirectOrderRequest.getProductId(), placeDirectOrderRequest.getQuantity(), token,corrId);
        logger.info("Inventory verified for product {}", placeDirectOrderRequest.getProductId());
        Order1 order1=new Order1();
        order1.setUserId(userId);
        order1.setAddressId(addressId);
        order1.setOrderType(OrderType.DIRECT_ORDER);
        order1.setOrderStatus(OrderStatus.PAYMENT_PENDING);
        order1.setPaymentStatus(PaymentStatus.PENDING);
        order1.setTotalAmount(product.getPrice()*placeDirectOrderRequest.getQuantity());
        

        OrderItem orderItem=new OrderItem();
        orderItem.setPrice(product.getPrice());
        orderItem.setQuantity(placeDirectOrderRequest.getQuantity());
        orderItem.setProductId(placeDirectOrderRequest.getProductId());
        orderItem.setSellerId(product.getSellerId());
        orderItem.setOrderItemStatus(OrderItemStatus.PAYMENT_PENDING);
        orderItem.setSubTotal(product.getPrice()*placeDirectOrderRequest.getQuantity());

        order1.addOrderItem(orderItem);
        orderRepo.save(order1);
        logger.info("Order {} created", order1.getId());

        inventoryClient.updateReservedStock(placeDirectOrderRequest.getProductId(), placeDirectOrderRequest.getQuantity(), token,corrId);
        logger.info("Reserved stock updated for product {}", placeDirectOrderRequest.getProductId());
        PaymentRequesDTO paymentRequesDTO=new PaymentRequesDTO();
        paymentRequesDTO.setAmount(order1.getTotalAmount());
        paymentRequesDTO.setOrderId(order1.getId());
        paymentRequesDTO.setPaymentMethod(placeDirectOrderRequest.getPaymentMethod());

        paymentClient.createPayment(paymentRequesDTO, token,corrId);
        logger.info("Payment initiated for order {}", order1.getId());


        return "Order created successfully";
    }


    public List<Order1> getMyOrders(){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        List<Order1> orders=orderRepo.findAllByUserId(userId);
        return orders;
        
    }

    public List<OrderItem> getOrderItems(Long orderId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long userId = userContext.getUserId();
        Order1 order1=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        if(!order1.getUserId().equals(userId)){
            throw new BadRequestException("You are not Authorized");
        }
        List<OrderItem> orderItem=orderItemRepo.findAllByOrder(order1);
        return orderItem;
    }

    @Transactional
    public String shipProduct(Long orderId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        Long userId=userContext.getUserId();
        Order1 order1=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        logger.info("Seller {} requested shipment for order {}", sellerId, orderId);

        if(order1.getOrderStatus() == OrderStatus.CANCELLED){
            throw new BadRequestException("Cancelled orders cannot be shipped");
        }
        List<OrderItem> orderItems=order1.getOrderItems();

        boolean shipped=false;
        for(OrderItem item:orderItems){
            
            if(item.getSellerId().equals(sellerId)){
            
                if(item.getOrderItemStatus() != OrderItemStatus.CONFIRMED){
                    throw new BadRequestException(
                        "Only confirmed products can be shipped");
                }
                item.setOrderItemStatus(OrderItemStatus.SHIPPED);
                logger.info("OrderItem {} shipped", item.getId());
                shipped=true;

                OrderItemStatusNotification notification=new OrderItemStatusNotification();
                notification.setUserId(userId);
                notification.setOrderId(orderId);
                notification.setOrderItemId(item.getId());
                notification.setProductId(item.getProductId());
                notification.setType(NotificationType.ORDERITEM_SHIPPED);
                notification.setMessage("Your "+ item.getProductId()+" has been shipped");

                kafkaTemplate.send("orderItem-shipped",notification);
            }
        }

        if (!shipped) {
            throw new BadRequestException("No products found for this seller in this order");
        }

        boolean isOrderShipped=true;
        for(OrderItem i:orderItems){
            if(i.getOrderItemStatus()!=OrderItemStatus.SHIPPED){
                isOrderShipped=false;
                break;
            }
        }

        if(isOrderShipped){

            OrderStatusNotification notification=new OrderStatusNotification();
            notification.setUserId(userId);
            notification.setOrderId(orderId);
            notification.setType(NotificationType.ORDER_SHIPPED);
            notification.setMessage("Your order has been placed");

            kafkaTemplate.send("order-shipped",notification);


            order1.setOrderStatus(OrderStatus.SHIPPED);
            logger.info("Order {} status changed to {}", orderId, order1.getOrderStatus());
            
        }
        else{
            order1.setOrderStatus(OrderStatus.PARTIALLY_SHIPPED);
            logger.info("Order {} status changed to {}", orderId, order1.getOrderStatus());
        }

        

        return "Product shipped successfully";
    }

    @Transactional
    public String deliverProduct(Long orderId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String role=userContext.getRole();
        if(!role.equals("ADMIN")){
            throw new AuthorizationException("You are not authorized");
        }
        
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        
        Order1 order1=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        logger.info("Admin delivering order {}", orderId);

        if(order1.getOrderStatus() == OrderStatus.CANCELLED){
            throw new BadRequestException("Cancelled orders cannot be shipped");
        }
        if(order1.getOrderStatus()==OrderStatus.CONFIRMED){
            throw new BadRequestException("This product is not yet shipped");
        }
        
        if(order1.getOrderStatus()==OrderStatus.PAYMENT_PENDING){
            throw new BadRequestException("This product's payment is still pending with the user");
        }

        
        List<OrderItem> orderItems=order1.getOrderItems();
        boolean delivered=false;
        for(OrderItem item:orderItems){
            if(item.getOrderItemStatus()==OrderItemStatus.SHIPPED){
                item.setOrderItemStatus(OrderItemStatus.DELIVERED);
                
                inventoryClient.releaseReservedStock(item.getProductId(), item.getQuantity(),token,corrId);
                logger.info("Reserved stock released for product {}", item.getProductId());
                delivered=true;
                OrderItemStatusNotification notification=new OrderItemStatusNotification();
                notification.setUserId(userId);
                notification.setOrderId(orderId);
                notification.setOrderItemId(item.getId());
                notification.setProductId(item.getProductId());
                notification.setType(NotificationType.ORDERITEM_DELIVERED);
                notification.setMessage("Your "+ item.getProductId()+" has been shipped");

                kafkaTemplate.send("orderItem-delivered",notification);
                logger.info("OrderItem {} delivered", item.getId());
            }
        }

        if(!delivered){
            throw new BadRequestException("No shipped products available for delivery");
        }

        boolean isOrderDelivered=true;
        for(OrderItem i:orderItems){
            if(i.getOrderItemStatus()!=OrderItemStatus.DELIVERED){
                isOrderDelivered=false;
                break;
            }
        }

        if(isOrderDelivered){
             OrderStatusNotification notification=new OrderStatusNotification();
            notification.setUserId(userId);
            notification.setOrderId(orderId);
            notification.setType(NotificationType.ORDER_DELIVERED);
            notification.setMessage("Your order has been placed");

            kafkaTemplate.send("order-delivered",notification);

            order1.setOrderStatus(OrderStatus.DELIVERED);
            logger.info("Order {} delivered successfully", orderId);
        }
        else{
            order1.setOrderStatus(OrderStatus.PARTIALLY_DELIVERED);
        }

        return "Product delivered";
    }

    @Transactional
    public String cancelOrder(Long orderId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        Order1 order1=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        logger.info("User {} requested cancellation for order {}", userId, orderId);
        if(!order1.getUserId().equals(userId)){
            throw new AuthorizationException("You are not authorized to cancel other's order");
        } 

        if(order1.getOrderStatus()!=OrderStatus.CONFIRMED && order1.getOrderStatus()!=OrderStatus.PAYMENT_PENDING){
            throw new BadRequestException("You dont have access to perform cancellation in this state");
        }

        List<OrderItem> orderItems=order1.getOrderItems();
        boolean cancelled=false;
        for(OrderItem item:orderItems){
            
            if(item.getOrderItemStatus()==OrderItemStatus.CONFIRMED || item.getOrderItemStatus()==OrderItemStatus.PAYMENT_PENDING){
                item.setOrderItemStatus(OrderItemStatus.CANCELLED);
                inventoryClient.cancelReservedStock(item.getProductId(), item.getQuantity(), token,corrId);
                logger.info("Reserved stock released"); 
                cancelled=true;
            }

        }

        if(!cancelled){
            throw new BadRequestException("No products available for cancellation");
        }

        
        paymentClient.cancelPayment(orderId, token,corrId);
        logger.info("Refund completed for order {}", orderId);
        order1.setOrderStatus(OrderStatus.CANCELLED);
        orderRepo.save(order1);
        logger.info("Order {} cancelled", orderId);

        return "Your order has been cancelled";

    }
    


    public List<SellerOrderResponse> sellerOrders(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        logger.info("Seller {} requested all orders", sellerId);
        List<OrderItem> orderItems=orderItemRepo.findBySellerId(sellerId);
        List <SellerOrderResponse> sellerOrderResponses=new ArrayList<>();
        
        for(OrderItem item: orderItems){
            SellerOrderResponse sellerOrderResponse=new SellerOrderResponse();
            sellerOrderResponse.setOrderId(item.getOrder().getId());
            sellerOrderResponse.setOrderItemId(item.getId());
            sellerOrderResponse.setOrderItemStatus(item.getOrderItemStatus());
            sellerOrderResponse.setOrderedAt(item.getOrderedAt());
            sellerOrderResponse.setPrice(item.getPrice());
            sellerOrderResponse.setProductId(item.getProductId());
            sellerOrderResponse.setQuantity(item.getQuantity());
            sellerOrderResponses.add(sellerOrderResponse);
        }

        return sellerOrderResponses;

    }

    public List<SellerOrderResponse> getOrderItemsByStatus(OrderItemStatus status){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        List<OrderItem> orderItems=orderItemRepo.findBySellerIdAndOrderItemStatus(sellerId,status);
        List <SellerOrderResponse> sellerOrderResponses=new ArrayList<>();
        logger.info("Seller {} requested {} orders", sellerId, status);
        for(OrderItem item: orderItems){
            SellerOrderResponse sellerOrderResponse=new SellerOrderResponse();
            sellerOrderResponse.setOrderId(item.getOrder().getId());
            sellerOrderResponse.setOrderItemId(item.getId());
            sellerOrderResponse.setOrderItemStatus(item.getOrderItemStatus());
            sellerOrderResponse.setOrderedAt(item.getOrderedAt());
            sellerOrderResponse.setPrice(item.getPrice());
            sellerOrderResponse.setProductId(item.getProductId());
            sellerOrderResponse.setQuantity(item.getQuantity());
            sellerOrderResponses.add(sellerOrderResponse);
        }

        return sellerOrderResponses;
    }

    public SellerDashboardDTO sellerdahboard(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        
        Long totalOrders=orderItemRepo.countBySellerId(sellerId);
        Long paymentPending=orderItemRepo.countBySellerIdAndOrderItemStatus(sellerId,OrderItemStatus.PAYMENT_PENDING);
        Long confirmed=orderItemRepo.countBySellerIdAndOrderItemStatus(sellerId, OrderItemStatus.CONFIRMED);
        Long shipped=orderItemRepo.countBySellerIdAndOrderItemStatus(sellerId, OrderItemStatus.SHIPPED);
        Long delivered=orderItemRepo.countBySellerIdAndOrderItemStatus(sellerId, OrderItemStatus.DELIVERED);
        Long cancelled=orderItemRepo.countBySellerIdAndOrderItemStatus(sellerId, OrderItemStatus.CANCELLED);

        SellerDashboardDTO sellerDashboardDTO=new SellerDashboardDTO();
        sellerDashboardDTO.setTotalOrders(totalOrders);
        sellerDashboardDTO.setPaymentPending(paymentPending);
        sellerDashboardDTO.setConfirmed(confirmed);
        sellerDashboardDTO.setShipped(shipped);
        sellerDashboardDTO.setDelivered(delivered);
        sellerDashboardDTO.setCancelled(cancelled);
        logger.info("Seller {} viewed dashboard", sellerId);
        return sellerDashboardDTO;

    } 

    @Transactional
    public String cancelByOrderItems(Long orderId, Long orderItemId){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext = (UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        Order1 order1=orderRepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found"));
        logger.info("User {} requested cancellation for orderItem {}", userId, orderItemId);
        if(!order1.getUserId().equals(userId)){
            throw new AuthorizationException("You are not authorized to cancel other's order");
        } 

        if(order1.getOrderStatus()==OrderStatus.DELIVERED || order1.getOrderStatus()==OrderStatus.SHIPPED){
            throw new BadRequestException("You can't cancel the order items which is already shipped or delivered");
        }
        
        List<OrderItem> orderItems=order1.getOrderItems();
        boolean isCancelled=false;
        for(OrderItem item:orderItems){
            if(item.getId().equals(orderItemId) ){
                if(item.getOrderItemStatus()==OrderItemStatus.CANCELLED ||item.getOrderItemStatus()==OrderItemStatus.SHIPPED ||item.getOrderItemStatus()==OrderItemStatus.DELIVERED){
                    throw new BadRequestException("You can't cancel the order items which is already shipped or delivered");
                }
                inventoryClient.cancelReservedStock(item.getProductId(), item.getQuantity(), token,corrId);
                logger.info("Reserved stock restored for product {}", item.getProductId());
                paymentClient.cancelPaymentForItem(orderId, item.getSubTotal(), token,corrId);
                logger.info("Refund updated for order {}", orderId);
                item.setOrderItemStatus(OrderItemStatus.CANCELLED);
                orderItemRepo.save(item);
                order1.setTotalAmount(order1.getTotalAmount() - item.getSubTotal());
                isCancelled=true;
                logger.info("OrderItem {} cancelled", orderItemId);
            }
        }

        if(!isCancelled){
            throw new ResourceNotFoundException("No items found");
        }
        

        return "Cancelled order item";

    }

}



// GET /cart/myCart
// PATCH /cart/updateQuantity
// DELETE /cart/remove/{productId}
// DELETE /cart/clear





// ✅ Address Module
// ✅ Seller Dashboard
// ✅ Buy Now cancellation
// ✅ Notification Service (start with synchronous calls, then migrate to Kafka)
// ✅ Final testing, cleanup, and documentation

