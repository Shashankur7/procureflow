package com.procureflow.supplier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
class SupplierService {
    private final SupplierRepository repository;

    SupplierService(SupplierRepository repository) { this.repository = repository; }

    @Transactional
    SupplierResponse create(CreateSupplierRequest command) {
        Supplier supplier = Supplier.create(command.name().trim(), command.email().trim(), command.taxIdentifier().trim());
        return SupplierResponse.from(repository.save(supplier));
    }

    @Transactional(readOnly = true)
    List<SupplierResponse> list() { return repository.findAllByOrderByNameAsc().stream().map(SupplierResponse::from).toList(); }
}
