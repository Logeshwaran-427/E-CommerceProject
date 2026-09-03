package com.project.INVENTORY.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.INVENTORY.Entity.InventoryTransactions;

@Repository
public interface InventoryTransactionRepo extends JpaRepository<InventoryTransactions,Long>{
    
}
