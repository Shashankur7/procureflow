package com.procureflow.inventory;

import com.procureflow.identity.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {
    private final InventoryService service;
    public InventoryController(InventoryService service) { this.service = service; }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROCUREMENT')")
    ProductResponse createProduct(@Valid @RequestBody CreateProductRequest command) { return service.createProduct(command); }

    @GetMapping("/inventory")
    List<InventoryResponse> inventory() { return service.listInventory(); }

    @GetMapping("/inventory/transactions")
    List<InventoryTransactionResponse> transactions() { return service.listTransactions(); }

    @PostMapping("/inventory/receipts")
    @PreAuthorize("hasRole('WAREHOUSE')")
    InventoryResponse receive(@Valid @RequestBody ReceiveStockRequest command, Authentication authentication) {
        return service.receive(command, ((UserPrincipal) authentication.getPrincipal()).id());
    }
}
