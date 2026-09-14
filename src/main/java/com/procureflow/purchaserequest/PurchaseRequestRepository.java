package com.procureflow.purchaserequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.UUID;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, UUID> {
    List<PurchaseRequest> findByRequesterIdOrderByCreatedAtDesc(UUID requesterId);

    List<PurchaseRequest> findByStatusOrderByCreatedAtAsc(PurchaseRequestStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select request from PurchaseRequest request where request.id = :id")
    java.util.Optional<PurchaseRequest> findByIdForDecision(UUID id);
}
