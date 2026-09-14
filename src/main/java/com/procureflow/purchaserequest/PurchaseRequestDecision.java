package com.procureflow.purchaserequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "purchase_request_decisions")
class PurchaseRequestDecision {
    @Id private UUID id;
    @Column(name = "purchase_request_id", nullable = false) private UUID purchaseRequestId;
    @Column(name = "decided_by", nullable = false) private UUID decidedBy;
    @Column(nullable = false) private String decision;
    @Column(length = 500) private String comment;
    @Column(name = "decided_at", nullable = false) private Instant decidedAt;

    protected PurchaseRequestDecision() { }

    static PurchaseRequestDecision record(UUID requestId, UUID managerId, PurchaseRequestStatus decision, String comment) {
        PurchaseRequestDecision result = new PurchaseRequestDecision();
        result.id = UUID.randomUUID();
        result.purchaseRequestId = requestId;
        result.decidedBy = managerId;
        result.decision = decision.name();
        result.comment = comment;
        result.decidedAt = Instant.now();
        return result;
    }

    UUID getId() { return id; }
    UUID getPurchaseRequestId() { return purchaseRequestId; }
    UUID getDecidedBy() { return decidedBy; }
    String getDecision() { return decision; }
    String getComment() { return comment; }
    Instant getDecidedAt() { return decidedAt; }
}
