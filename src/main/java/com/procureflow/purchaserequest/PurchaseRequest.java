package com.procureflow.purchaserequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "purchase_requests")
public class PurchaseRequest {
    @Id
    private UUID id;
    @Column(nullable = false, length = 160)
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(name = "estimated_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal estimatedAmount;
    @Column(nullable = false, length = 3)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseRequestStatus status;
    @Column(name = "requester_id", nullable = false)
    private UUID requesterId;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected PurchaseRequest() { }

    static PurchaseRequest submit(String title, String description, BigDecimal estimatedAmount,
                                  String currency, UUID requesterId) {
        PurchaseRequest request = new PurchaseRequest();
        request.id = UUID.randomUUID();
        request.title = title;
        request.description = description;
        request.estimatedAmount = estimatedAmount;
        request.currency = currency;
        request.status = PurchaseRequestStatus.PENDING_APPROVAL;
        request.requesterId = requesterId;
        request.createdAt = Instant.now();
        request.updatedAt = request.createdAt;
        return request;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public BigDecimal getEstimatedAmount() { return estimatedAmount; }
    public String getCurrency() { return currency; }
    public PurchaseRequestStatus getStatus() { return status; }
    public UUID getRequesterId() { return requesterId; }
    public Instant getCreatedAt() { return createdAt; }

    void decide(PurchaseRequestStatus decision) {
        if (status != PurchaseRequestStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only pending requests can be decided");
        }
        status = decision;
        updatedAt = Instant.now();
    }
}
