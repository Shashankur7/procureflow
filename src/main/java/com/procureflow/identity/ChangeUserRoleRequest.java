package com.procureflow.identity;

import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(@NotNull UserRole role) { }
