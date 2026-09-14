package com.procureflow.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
class Product {
    @Id private UUID id;
    @Column(nullable = false, unique = true, length = 50) private String sku;
    @Column(nullable = false, length = 160) private String name;
    @Column(name = "reorder_level", nullable = false) private int reorderLevel;
    @Column(nullable = false) private boolean active;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    protected Product() { }
    static Product create(String sku, String name, int reorderLevel) {
        Product product = new Product();
        product.id = UUID.randomUUID(); product.sku = sku; product.name = name;
        product.reorderLevel = reorderLevel; product.active = true; product.createdAt = Instant.now();
        return product;
    }
    UUID getId() { return id; }
    String getSku() { return sku; }
    String getName() { return name; }
    int getReorderLevel() { return reorderLevel; }
    boolean isActive() { return active; }
}
