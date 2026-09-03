package com.project.ORDER.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ORDER.Entities.Order1;
import com.project.ORDER.Entities.OrderItem;
import com.project.ORDER.Enums.OrderItemStatus;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {

    Optional<OrderItem> findByOrderAndProductId(Order1 order,Long productId);

    List<OrderItem> findAllByOrder(Order1 order);

    List<OrderItem> findBySellerId(Long sellerId);

    List<OrderItem> findBySellerIdAndOrderItemStatus(Long sellerId, OrderItemStatus status);

    Long countBySellerId(Long sellerId);

    Long countBySellerIdAndOrderItemStatus(Long sellerId,OrderItemStatus orderItemStatus);
    
}
