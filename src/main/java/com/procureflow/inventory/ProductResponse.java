package com.procureflow.inventory;

import java.util.UUID;

public record ProductResponse(UUID id, String sku, String name, int reorderLevel, boolean active) {
    static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSku(), product.getName(), product.getReorderLevel(), product.isActive());
    }
}
