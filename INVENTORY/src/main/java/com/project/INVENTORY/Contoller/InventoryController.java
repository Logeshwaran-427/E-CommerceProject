package com.project.INVENTORY.Contoller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.INVENTORY.DTO.InventoryRequestDTO;
import com.project.INVENTORY.Entity.Inventory;
import com.project.INVENTORY.Service.InventoryService;


@RestController
@RequestMapping("/inventory")
public class InventoryController {

    final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService=inventoryService;
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    @PostMapping("/createInventory")
    public String createInventory(@RequestBody InventoryRequestDTO inventoryRequestDTO) { 
        return inventoryService.createInventory(inventoryRequestDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    @PatchMapping("/addStock")
    public String addStock(@RequestBody InventoryRequestDTO inventoryRequestDTO){
        return inventoryService.addStock(inventoryRequestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/getAllInventory")
    public List<Inventory> getAllInventory(){
        return inventoryService.getAllInventory();
    }

    @PreAuthorize("hasRole('PRODUCT_OWNER')")
    @GetMapping("/seller/getInventory")
    public List<Inventory> getInventory(@RequestParam int page){
        return inventoryService.getMyInventory(page);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER')")
    @PatchMapping("/deleteInventory/{productId}")
    public String deleteInventory(@PathVariable Long productId){
        return inventoryService.deleteInventory(productId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','USER')")
    @GetMapping("/getInventoryById/{id}")
    public Inventory getInventoryById(@PathVariable Long id) {
        return inventoryService.getByProductId(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','USER')")
    @GetMapping("/stockStatus/{id}")
    public boolean checkStock(@PathVariable Long id, @RequestParam Integer stock) {
        return inventoryService.checkStock(id, stock);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','USER')")
    @PatchMapping("/updateReservedStock/{id}")
    public void updateReservedStock(@PathVariable Long id, @RequestParam Integer stock){
        inventoryService.addReservedStock(id, stock);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','USER')")
    @PostMapping("/checkMultipleInventory")
    public boolean postMethodName(@RequestBody List<InventoryRequestDTO> inventoryRequestDTOs) {
        return inventoryService.checkMultipleStocks(inventoryRequestDTOs);
    }

    @PreAuthorize("hasAnyRole('ADMIN','PRODUCT_OWNER','USER')")
    @PatchMapping("/updateMultipleReservedStocks")
    public void updateMultipleReservedStocks(@RequestBody List<InventoryRequestDTO> inventoryRequestDTOs){
        inventoryService.addMultipleResveredStock(inventoryRequestDTOs);
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/releaseReservedStock/{productId}")
    public void releaseReservedStock(@PathVariable Long productId,@RequestParam Integer quantity){
        inventoryService.releaseReservedStock(productId, quantity);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/cancelReservedStock/{productId}")
    public void cancelReservedStock(@PathVariable Long productId,@RequestParam Integer quantity){
        inventoryService.cancelReservedStock(productId, quantity);
    }
    


    
      
}
