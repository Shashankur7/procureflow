package com.procureflow.identity;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

@Service
class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String bootstrapAdminEmail;

    AuthService(AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService,
                @Value("${app.bootstrap-admin-email}") String bootstrapAdminEmail) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.bootstrapAdminEmail = bootstrapAdminEmail.trim().toLowerCase(Locale.ROOT);
    }

    @Transactional
    TokenResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already exists for this email");
        }
        UserRole role = email.equals(bootstrapAdminEmail) && !bootstrapAdminEmail.isEmpty() ? UserRole.ADMIN : UserRole.EMPLOYEE;
        AppUser user = AppUser.register(email, request.fullName().trim(), passwordEncoder.encode(request.password()), role);
        users.save(user);
        return tokenFor(user);
    }

    @Transactional(readOnly = true)
    TokenResponse login(LoginRequest request) {
        AppUser user = users.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }
        return tokenFor(user);
    }

    private TokenResponse tokenFor(AppUser user) {
        return new TokenResponse(jwtService.createToken(user), "Bearer", jwtService.expiresInSeconds());
    }
}
