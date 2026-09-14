package com.procureflow.identity;

import java.util.UUID;

public record UserPrincipal(UUID id, String email, UserRole role) { }
