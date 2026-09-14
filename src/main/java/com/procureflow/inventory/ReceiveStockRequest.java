package com.procureflow.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ReceiveStockRequest(@NotNull UUID productId, @Min(1) int quantity,
                                  @Size(max = 500) String referenceNote) { }
