package com.procureflow.purchaseorder;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePurchaseOrder(@NotNull UUID purchaseRequestId, @NotNull UUID supplierId) { }
