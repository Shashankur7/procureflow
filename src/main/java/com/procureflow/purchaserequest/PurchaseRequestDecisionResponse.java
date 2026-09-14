package com.procureflow.purchaserequest;

import java.time.Instant;
import java.util.UUID;

public record PurchaseRequestDecisionResponse(UUID id, UUID purchaseRequestId, UUID decidedBy,
                                              String decision, String comment, Instant decidedAt) {
    static PurchaseRequestDecisionResponse from(PurchaseRequestDecision decision) {
        return new PurchaseRequestDecisionResponse(decision.getId(), decision.getPurchaseRequestId(),
                decision.getDecidedBy(), decision.getDecision(), decision.getComment(), decision.getDecidedAt());
    }
}
