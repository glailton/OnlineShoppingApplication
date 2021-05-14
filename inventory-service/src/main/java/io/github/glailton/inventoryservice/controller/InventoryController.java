package io.github.glailton.inventoryservice.controller;

import io.github.glailton.inventoryservice.model.Inventory;
import io.github.glailton.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}")
    Boolean isInStock(@PathVariable String skuCode) {
        Inventory inventory = inventoryService.findBySkuCode(skuCode)
                .orElseThrow(() -> new RuntimeException("Cannot Find Product by sku code " + skuCode));

        return inventory.getStock() > 0;
    }
}
