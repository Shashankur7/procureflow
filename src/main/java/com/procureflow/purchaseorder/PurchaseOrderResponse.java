package com.procureflow.purchaseorder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PurchaseOrderResponse(UUID id, String poNumber, UUID purchaseRequestId, UUID supplierId,
                                    BigDecimal totalAmount, String currency, PurchaseOrderStatus status, Instant createdAt) {
    static PurchaseOrderResponse from(PurchaseOrder order) {
        return new PurchaseOrderResponse(order.getId(), order.getPoNumber(), order.getPurchaseRequestId(),
                order.getSupplierId(), order.getTotalAmount(), order.getCurrency(), order.getStatus(), order.getCreatedAt());
    }
}
