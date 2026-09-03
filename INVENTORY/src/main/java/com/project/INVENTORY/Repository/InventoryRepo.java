package com.project.INVENTORY.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.INVENTORY.Entity.Inventory;
import com.project.INVENTORY.Enums.InventoryStatus;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {
    
    boolean existsByProductId(Long productId);
    Optional<Inventory> findByProductId(Long productId);

    List<Inventory> findByProductIdIn(List<Long> productIds);

    Optional<Inventory> findByProductIdAndStatusNot(Long productId, InventoryStatus status);
}
