package com.procureflow.supplier;

import java.util.UUID;

public record SupplierResponse(UUID id, String name, String email, String taxIdentifier, boolean active) {
    static SupplierResponse from(Supplier supplier) {
        return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getEmail(),
                supplier.getTaxIdentifier(), supplier.isActive());
    }
}
