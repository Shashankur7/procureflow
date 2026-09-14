package com.procureflow.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSupplierRequest(
        @NotBlank @Size(max = 160) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(max = 50) String taxIdentifier
) { }
