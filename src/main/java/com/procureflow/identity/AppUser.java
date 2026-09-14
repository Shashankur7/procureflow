package com.procureflow.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "app_users")
class AppUser {
    @Id private UUID id;
    @Column(nullable = false, unique = true) private String email;
    @Column(name = "full_name", nullable = false) private String fullName;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private UserRole role;
    @Column(name = "created_at", nullable = false) private Instant createdAt;

    protected AppUser() { }

    static AppUser register(String email, String fullName, String passwordHash, UserRole role) {
        AppUser user = new AppUser();
        user.id = UUID.randomUUID();
        user.email = email;
        user.fullName = fullName;
        user.passwordHash = passwordHash;
        user.role = role;
        user.createdAt = Instant.now();
        return user;
    }

    UUID getId() { return id; }
    String getEmail() { return email; }
    String getPasswordHash() { return passwordHash; }
    UserRole getRole() { return role; }
    String getFullName() { return fullName; }
    void changeRole(UserRole role) { this.role = role; }
}
