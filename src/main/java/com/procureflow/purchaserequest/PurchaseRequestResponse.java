package com.procureflow.purchaserequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PurchaseRequestResponse(UUID id, String title, String description,
                                      BigDecimal estimatedAmount, String currency,
                                      PurchaseRequestStatus status, UUID requesterId,
                                      Instant createdAt) {
    static PurchaseRequestResponse from(PurchaseRequest request) {
        return new PurchaseRequestResponse(request.getId(), request.getTitle(), request.getDescription(),
                request.getEstimatedAmount(), request.getCurrency(), request.getStatus(),
                request.getRequesterId(), request.getCreatedAt());
    }
}
