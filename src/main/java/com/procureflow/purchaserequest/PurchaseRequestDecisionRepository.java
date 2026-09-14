package com.procureflow.purchaserequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PurchaseRequestDecisionRepository extends JpaRepository<PurchaseRequestDecision, UUID> { }
