package com.procureflow.purchaseorder;

import com.procureflow.identity.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-orders")
public class PurchaseOrderController {
    private final PurchaseOrderService service;

    public PurchaseOrderController(PurchaseOrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PROCUREMENT')")
    PurchaseOrderResponse create(@Valid @RequestBody CreatePurchaseOrder command, Authentication authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        return service.create(command, user.id());
    }

    @GetMapping
    @PreAuthorize("hasRole('PROCUREMENT')")
    List<PurchaseOrderResponse> list() { return service.list(); }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('PROCUREMENT')")
    PurchaseOrderResponse changeStatus(@PathVariable java.util.UUID orderId,
                                       @Valid @RequestBody UpdatePurchaseOrderStatus command) {
        return service.changeStatus(orderId, command);
    }
}
