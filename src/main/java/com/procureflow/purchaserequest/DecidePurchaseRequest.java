package com.procureflow.purchaserequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DecidePurchaseRequest(
        @NotNull Decision decision,
        @Size(max = 500) String comment
) {
    public enum Decision { APPROVE, REJECT }
}
