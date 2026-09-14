package com.procureflow.purchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    boolean existsByPurchaseRequestId(UUID purchaseRequestId);
    java.util.List<PurchaseOrder> findAllByOrderByCreatedAtDesc();
}
