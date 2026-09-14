package com.procureflow.purchaserequest;

import jakarta.validation.Valid;
import com.procureflow.identity.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-requests")
public class PurchaseRequestController {
    private final PurchaseRequestService service;

    public PurchaseRequestController(PurchaseRequestService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PurchaseRequestResponse create(@Valid @RequestBody CreatePurchaseRequest command,
                                   Authentication authentication) {
        return service.create(command, currentUser(authentication).id());
    }

    @GetMapping("/mine")
    List<PurchaseRequestResponse> mine(Authentication authentication) {
        return service.findForRequester(currentUser(authentication).id());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('MANAGER')")
    List<PurchaseRequestResponse> pending() {
        return service.findPending();
    }

    @GetMapping("/approved")
    @PreAuthorize("hasRole('PROCUREMENT')")
    List<PurchaseRequestResponse> approved() { return service.findApproved(); }

    @PostMapping("/{requestId}/decision")
    @PreAuthorize("hasRole('MANAGER')")
    PurchaseRequestDecisionResponse decide(@PathVariable UUID requestId,
                                           @Valid @RequestBody DecidePurchaseRequest command,
                                           Authentication authentication) {
        return service.decide(requestId, command, currentUser(authentication).id());
    }

    private UserPrincipal currentUser(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }
}
