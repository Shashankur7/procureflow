package com.procureflow.inventory;

import java.time.Instant;
import java.util.UUID;

public record InventoryTransactionResponse(UUID id, UUID productId, String productName, String sku,
                                           String transactionType, int quantityDelta, String referenceNote,
                                           UUID performedBy, Instant createdAt) { }
