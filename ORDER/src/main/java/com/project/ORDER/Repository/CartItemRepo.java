package com.project.ORDER.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ORDER.Entities.Cart;
import com.project.ORDER.Entities.CartItems;
import java.util.List;
import java.util.Optional;


@Repository
public interface CartItemRepo extends JpaRepository<CartItems,Long>{

    boolean existsByProductId(Long productId);

    Optional<CartItems> findByCartAndProductId(Cart cart,Long productId);

    List<CartItems> findAllByCart(Cart cart);

    List<CartItems> findAllByCartAndProductIdIn(Cart cart,List<Long> productIds);
    
}
