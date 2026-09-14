package com.procureflow.purchaseorder;

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
@Table(name = "purchase_orders")
class PurchaseOrder {
    @Id private UUID id;
    @Column(name = "po_number", nullable = false, unique = true) private String poNumber;
    @Column(name = "purchase_request_id", nullable = false, unique = true) private UUID purchaseRequestId;
    @Column(name = "supplier_id", nullable = false) private UUID supplierId;
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2) private BigDecimal totalAmount;
    @Column(nullable = false, length = 3) private String currency;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PurchaseOrderStatus status;
    @Column(name = "created_by", nullable = false) private UUID createdBy;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    protected PurchaseOrder() { }

    static PurchaseOrder create(String poNumber, UUID requestId, UUID supplierId, BigDecimal totalAmount,
                                String currency, UUID createdBy) {
        PurchaseOrder order = new PurchaseOrder();
        order.id = UUID.randomUUID();
        order.poNumber = poNumber;
        order.purchaseRequestId = requestId;
        order.supplierId = supplierId;
        order.totalAmount = totalAmount;
        order.currency = currency;
        order.status = PurchaseOrderStatus.DRAFT;
        order.createdBy = createdBy;
        order.createdAt = Instant.now();
        return order;
    }

    UUID getId() { return id; }
    String getPoNumber() { return poNumber; }
    UUID getPurchaseRequestId() { return purchaseRequestId; }
    UUID getSupplierId() { return supplierId; }
    BigDecimal getTotalAmount() { return totalAmount; }
    String getCurrency() { return currency; }
    PurchaseOrderStatus getStatus() { return status; }
    UUID getCreatedBy() { return createdBy; }
    Instant getCreatedAt() { return createdAt; }

    void changeStatus(PurchaseOrderStatus nextStatus) {
        this.status = nextStatus;
    }
}
