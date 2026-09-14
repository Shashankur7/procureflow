package com.procureflow.inventory;

import java.util.UUID;

public record InventoryResponse(UUID productId, String sku, String name, int quantityOnHand,
                                int reorderLevel, boolean lowStock) { }
