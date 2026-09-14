package com.procureflow.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id private UUID id;
    @Column(nullable = false, length = 160) private String name;
    @Column(nullable = false) private String email;
    @Column(name = "tax_identifier", nullable = false, unique = true) private String taxIdentifier;
    @Column(nullable = false) private boolean active;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;

    protected Supplier() { }

    static Supplier create(String name, String email, String taxIdentifier) {
        Supplier supplier = new Supplier();
        supplier.id = UUID.randomUUID();
        supplier.name = name;
        supplier.email = email;
        supplier.taxIdentifier = taxIdentifier;
        supplier.active = true;
        supplier.createdAt = Instant.now();
        return supplier;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getTaxIdentifier() { return taxIdentifier; }
    public boolean isActive() { return active; }
}
