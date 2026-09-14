package com.procureflow.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(@NotBlank @Size(max = 50) String sku,
                                   @NotBlank @Size(max = 160) String name,
                                   @Min(0) int reorderLevel) { }
