package com.procureflow.purchaseorder;

import jakarta.validation.constraints.NotNull;

public record UpdatePurchaseOrderStatus(@NotNull PurchaseOrderStatus status) { }
