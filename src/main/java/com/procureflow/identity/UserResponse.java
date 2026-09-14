package com.procureflow.identity;

import java.util.UUID;

public record UserResponse(UUID id, String email, String fullName, UserRole role) {
    static UserResponse from(AppUser user) { return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole()); }
}
