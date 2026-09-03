package com.project.INVENTORY.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.INVENTORY.Client.InventoryClient;
import com.project.INVENTORY.DTO.InventoryRequestDTO;
import com.project.INVENTORY.DTO.ProductClient;
import com.project.INVENTORY.DTO.UserContext;
import com.project.INVENTORY.Entity.Inventory;
import com.project.INVENTORY.Entity.InventoryTransactions;
import com.project.INVENTORY.Enums.InventoryStatus;
import com.project.INVENTORY.Enums.ProductStatus;
import com.project.INVENTORY.Enums.TransactionType;
import com.project.INVENTORY.ExceptionHandling.AuthorizationException;
import com.project.INVENTORY.ExceptionHandling.BadRequestException;
import com.project.INVENTORY.ExceptionHandling.ResourceNotFoundException;
import com.project.INVENTORY.Repository.InventoryRepo;
import com.project.INVENTORY.Repository.InventoryTransactionRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class InventoryService {

    final InventoryRepo inventoryRepo;

    final InventoryTransactionRepo inventoryTransactionRepo;

    final InventoryClient inventoryClient;

    final HttpServletRequest request;

    Logger logger=LoggerFactory.getLogger(InventoryService.class);

    InventoryService(InventoryRepo inventoryRepo, InventoryTransactionRepo inventoryTransactionRepo,InventoryClient inventoryClient,HttpServletRequest request) {
        this.inventoryRepo = inventoryRepo;
        this.inventoryTransactionRepo = inventoryTransactionRepo;
        this.inventoryClient=inventoryClient;
        this.request=request;
    }

    


    @Transactional
    public String createInventory(InventoryRequestDTO inventoryRequestDTO){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();

        UserContext userContext=(UserContext) auth.getDetails();

        Long sellerId= userContext.getSellerId();
        Long userId=userContext.getUserId();
        logger.info("Inventory creation requested. productId={}, sellerId={}",inventoryRequestDTO.getProductId(),sellerId);
        boolean isAdmin=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        ProductClient product=inventoryClient.findProduct(inventoryRequestDTO.getProductId(),token,corrId);

        if(product.getStatus()!=ProductStatus.ACTIVE){
            throw new BadRequestException("please enter the active product");
        }

        if(inventoryRepo.existsByProductId(product.getId())){
            throw new BadRequestException("Product id already exists");
        }

        if(!isAdmin){
            if(!product.getSellerId().equals(sellerId)){
                throw new AuthorizationException("You are not the owner for the product. Please enter the appropriate product id of yours");
            }
        }

        Inventory inventory=new Inventory();
        inventory.setProductId(product.getId());
        if(inventoryRequestDTO.getStock()<0){
            throw new BadRequestException("Stock cannot be negative");
        }
        inventory.setAvailableQuantity(inventoryRequestDTO.getStock());
        if(inventoryRequestDTO.getStock()>10){
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
        else if(inventoryRequestDTO.getStock()==0){
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        }
        else{
            inventory.setStatus(InventoryStatus.LOW_STOCK);
        }
        inventory.setReservedQuantity(0);
        inventoryRepo.save(inventory);
        logger.info("Inventory created successfully. productId={}, availableStock={}",inventory.getProductId(),inventory.getAvailableQuantity());

        
        InventoryTransactions inventoryTransactions=new InventoryTransactions();
        inventoryTransactions.setProductId(inventory.getProductId());
        inventoryTransactions.setQuantity(inventory.getAvailableQuantity());
        inventoryTransactions.setTransactionType(TransactionType.STOCK_ADDED);
        inventoryTransactions.setRemarks("Initial inventory creation");
        inventoryTransactions.setPerformedBy(userId);

        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Inventory transaction recorded for productId={}",inventory.getProductId());
        return "Stock added successfully";
    }

    @Transactional
    public String addStock(InventoryRequestDTO inventoryRequestDTO){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        Long sellerId=userContext.getSellerId();
        logger.info("Stock update requested. productId={}, sellerId={}, additionalStock={}",inventoryRequestDTO.getProductId(),sellerId,inventoryRequestDTO.getStock());
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        boolean isAdmin=auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));

        ProductClient product=inventoryClient.findProduct(inventoryRequestDTO.getProductId(),token,corrId );

        if(product.getStatus()!=ProductStatus.ACTIVE){
            throw new BadRequestException("Product is not active. Please provide valid product id");
        }

        if(!isAdmin){
            if(!product.getSellerId().equals(sellerId)){
                throw new AuthorizationException("you are not authorized to perform this action");
            }
        }

        if(inventoryRequestDTO.getStock()<0){
            throw new BadRequestException("Stock value should be positive");
        }

        Inventory inventory=inventoryRepo.findByProductId(product.getId()).orElseThrow(()->new ResourceNotFoundException("Inventory for the product not found"));
        Integer oldStock=inventory.getAvailableQuantity();
        Integer newStock=inventoryRequestDTO.getStock()+oldStock;
        inventory.setAvailableQuantity(newStock);

        if(inventory.getAvailableQuantity()>10){
            inventory.setStatus(InventoryStatus.IN_STOCK);
        }
        else if(inventory.getAvailableQuantity()==0){
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        }
        else{
            inventory.setStatus(InventoryStatus.LOW_STOCK);
        }
        


        inventoryRepo.save(inventory);
        logger.info("Stock updated successfully. productId={}, newAvailableStock={}",inventory.getProductId(),inventory.getAvailableQuantity());
        InventoryTransactions inventoryTransactions=new InventoryTransactions();

        inventoryTransactions.setPerformedBy(userId);
        inventoryTransactions.setProductId(inventory.getProductId());
        inventoryTransactions.setQuantity(inventoryRequestDTO.getStock());
        inventoryTransactions.setTransactionType(TransactionType.STOCK_ADDED);
        inventoryTransactions.setRemarks("Stock updated");
        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Stock addition transaction recorded. productId={}",inventory.getProductId());

        return "Stock updated successfully";
        
    }

    public List<Inventory> getAllInventory(){
        logger.info("Fetching all inventory records");
        return inventoryRepo.findAll();
    }

    public List<Inventory> getMyInventory(int page){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long sellerId=userContext.getSellerId();
        String token=request.getHeader("Authorization");
        String corrId=request.getHeader("X-Correlation-ID");
        logger.info("Fetching inventory for sellerId={}", sellerId);

        List<ProductClient> products=inventoryClient.currentSellerProducts(token,page,corrId);
        
        List<Long> productIds=new ArrayList<>();
        for(ProductClient productId:products){
            if(productId.getSellerId().equals(sellerId)){
                productIds.add(productId.getId());
            }
            
        }
        logger.info("{} inventory products returned for sellerId={}",productIds.size(),sellerId);


        return inventoryRepo.findByProductIdIn(productIds);
    }
    
    public String deleteInventory(Long id){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Inventory deactivation requested. productId={}", id);
        Inventory inventory=inventoryRepo.findByProductId(id).orElseThrow(()-> new ResourceNotFoundException("Inventory not found"));
        inventory.setStatus(InventoryStatus.INACTIVE);
        inventoryRepo.save(inventory);
        InventoryTransactions inventoryTransactions=new InventoryTransactions();
        inventoryTransactions.setProductId(id);
        inventoryTransactions.setTransactionType(TransactionType.PRODUCT_DEACTIVATED);
        inventoryTransactions.setPerformedBy(userId);
        inventoryTransactions.setRemarks("Product deactivated");
        inventoryTransactions.setQuantity(inventory.getAvailableQuantity());
        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Inventory deactivated successfully. productId={}", id);
        return "Inventory deactivated";
    }


    public Inventory getByProductId(Long productId){
        logger.info("Fetching inventory by productId={}", productId);
        return inventoryRepo.findByProductIdAndStatusNot(productId,InventoryStatus.INACTIVE).orElseThrow(()->new ResourceNotFoundException("InventoryNot foung dor the product id."));
    }

    public boolean checkStock(Long productId,Integer quantity){
        logger.info("Checking stock. productId={}, requestedQuantity={}",productId,quantity);
        Inventory inventory=inventoryRepo.findByProductIdAndStatusNot(productId,InventoryStatus.INACTIVE).orElseThrow(()->new ResourceNotFoundException("Inventory Not found for the product id."));
        if(quantity>inventory.getAvailableQuantity()){
            throw new BadRequestException("Currently we don't have the required product you have provided. The remaining stock level is "+inventory.getAvailableQuantity());
        }
        logger.info("Stock verification successful. productId={}", productId);
        return true;
    }

    @Transactional
    public void addReservedStock(Long productId, Integer quantity){

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        logger.info("Stock reservation requested. productId={}, quantity={}",productId,quantity);
        Inventory inventory=inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        Integer availableQuantity=inventory.getAvailableQuantity();
        if(quantity>availableQuantity){
            throw new BadRequestException("Insufficient stock");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity()+quantity);
        inventory.setAvailableQuantity(availableQuantity-quantity);
        inventoryRepo.save(inventory);
        logger.info("Stock reserved successfully. productId={}, reserved={}, available={}",productId,inventory.getReservedQuantity(),inventory.getAvailableQuantity());
        
        InventoryTransactions inventoryTransactions=new InventoryTransactions();
        inventoryTransactions.setProductId(productId);
        inventoryTransactions.setQuantity(quantity);
        inventoryTransactions.setTransactionType(TransactionType.STOCK_RESERVED);
        inventoryTransactions.setPerformedBy(userId);
        inventoryTransactions.setRemarks("Stock reserved");
        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Reservation transaction recorded. productId={}", productId);
        
    }

    public boolean checkMultipleStocks(List<InventoryRequestDTO> productIds){
        logger.info("Checking inventory for {} products",productIds.size());
        for(InventoryRequestDTO i:productIds){
            Inventory inventory=inventoryRepo.findByProductIdAndStatusNot(i.getProductId(),InventoryStatus.INACTIVE).orElseThrow(()->new ResourceNotFoundException("Inventory Not found for the product id."));
        if(i.getStock()>inventory.getAvailableQuantity()){
            throw new BadRequestException("Insufficient stock for product " + i.getProductId());
        }

        }
        logger.info("Stock verification successful for all requested products");
        return true;

    }

    public void addMultipleResveredStock(List<InventoryRequestDTO> inventoryRequestDTOs){
        logger.info("Bulk stock reservation requested for {} products",inventoryRequestDTOs.size());
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();

        for(InventoryRequestDTO req:inventoryRequestDTOs){
            Inventory inventory=inventoryRepo.findByProductId(req.getProductId()).orElseThrow(()->new ResourceNotFoundException( "Product not found"));
            Integer availableQuantity=inventory.getAvailableQuantity();
            if(req.getStock()>availableQuantity){
                throw new BadRequestException("Insufficient stock");
            }
            inventory.setReservedQuantity(inventory.getReservedQuantity()+req.getStock());
            inventory.setAvailableQuantity(availableQuantity-req.getStock());
            inventoryRepo.save(inventory);
            logger.info("Reserved stock updated. productId={}, reserved={}, available={}",req.getProductId(),inventory.getReservedQuantity(),inventory.getAvailableQuantity());

            InventoryTransactions inventoryTransactions=new InventoryTransactions();
            inventoryTransactions.setProductId(req.getProductId());
            inventoryTransactions.setQuantity(req.getStock());
            inventoryTransactions.setTransactionType(TransactionType.STOCK_RESERVED);
            inventoryTransactions.setPerformedBy(userId);
            inventoryTransactions.setRemarks("Stock reserved");
            inventoryTransactionRepo.save(inventoryTransactions);
        }
        logger.info("Bulk stock reservation completed successfully");

    }


    public void releaseReservedStock(Long productId,Integer quantity){

        logger.info("Reserved stock release requested. productId={}, quantity={}",productId,quantity);
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();
        String role=userContext.getRole();
        
        if(!role.equals("ADMIN")){
            throw new AuthorizationException("You are not authorized to perform this acction");
        }

        Inventory inventory=inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));

        inventory.setReservedQuantity(inventory.getReservedQuantity()-quantity);
        inventoryRepo.save(inventory);
        logger.info("Reserved stock released. productId={}, remainingReserved={}",productId,inventory.getReservedQuantity());

        InventoryTransactions inventoryTransactions=new InventoryTransactions();
        inventoryTransactions.setProductId(inventory.getProductId());
        inventoryTransactions.setQuantity(quantity);
        inventoryTransactions.setTransactionType(TransactionType.STOCK_REMOVED);
        inventoryTransactions.setPerformedBy(userId);
        inventoryTransactions.setRemarks("Cleared reserved stock");
        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Reserved stock release transaction recorded. productId={}",productId);
    }


    public void cancelReservedStock(Long productId,Integer quantity){
        logger.info("Reserved stock cancellation requested. productId={}, quantity={}",productId,quantity);
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        UserContext userContext=(UserContext) auth.getDetails();
        Long userId=userContext.getUserId();

        Inventory inventory=inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));

        inventory.setReservedQuantity(inventory.getReservedQuantity()-quantity);
        inventory.setAvailableQuantity(inventory.getAvailableQuantity()+quantity);
        inventoryRepo.save(inventory);
        logger.info("Reserved stock restored successfully. productId={}, available={}, reserved={}",productId,inventory.getAvailableQuantity(),inventory.getReservedQuantity());

        InventoryTransactions inventoryTransactions=new InventoryTransactions();
        inventoryTransactions.setProductId(inventory.getProductId());
        inventoryTransactions.setQuantity(quantity);
        inventoryTransactions.setTransactionType(TransactionType.STOCK_RELEASED);
        inventoryTransactions.setPerformedBy(userId);
        inventoryTransactions.setRemarks("Cancelled reserved stock");
        inventoryTransactionRepo.save(inventoryTransactions);
        logger.info("Stock release transaction recorded. productId={}",productId);
    }

}


// 4. Concurrency

// One important question:

// Does your repository use

// @Lock(LockModeType.PESSIMISTIC_WRITE)

// for

// findByProductId(...)

// If not, there is still a race condition.

// Example

// Available = 5

// User A
// reads 5

// User B
// reads 5

// Both reserve 5

// Available = -5

// You previously mentioned you wanted to use pessimistic locking.

// This is exactly the place to use it.




// Inventory Audit APIs
// Inventory History APIs
// Bulk Stock Import
// Warehouse Support
// Multi-location Inventory
// Inventory Alerts
// Inventory Analytics
// Low Stock Notifications


// Implement later