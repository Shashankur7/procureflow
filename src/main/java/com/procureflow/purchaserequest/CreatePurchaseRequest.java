package com.procureflow.purchaserequest;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePurchaseRequest(
        @NotBlank @Size(max = 160) String title,
        @Size(max = 1000) String description,
        @NotNull @DecimalMin(value = "0.01") BigDecimal estimatedAmount,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$", message = "must be a three-letter ISO currency code") String currency
) { }
