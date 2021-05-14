package io.github.glailton.inventoryservice.service;

import io.github.glailton.inventoryservice.model.Inventory;
import io.github.glailton.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Optional<Inventory> findBySkuCode(String skuCode){
        log.info("Checking stock for product with skucode - " + skuCode);
        return inventoryRepository.findBySkuCode(skuCode);
    }
}
