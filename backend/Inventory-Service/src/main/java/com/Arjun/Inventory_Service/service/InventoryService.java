package com.Arjun.Inventory_Service.service;

import com.Arjun.Inventory_Service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final  InventoryRepository inventoryRepository;
    public boolean isInStock(String skuCode,Integer quantity){
       return inventoryRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode,quantity);
    }
}
