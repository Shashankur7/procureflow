package com.procureflow.identity;

import org.springframework.http.HttpStatus;
import com.procureflow.common.audit.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
class UserAdministrationService {
    private final AppUserRepository users;
    private final ActivityService activity;
    UserAdministrationService(AppUserRepository users, ActivityService activity) { this.users = users; this.activity = activity; }

    @Transactional(readOnly = true)
    List<UserResponse> list() { return users.findAll().stream().map(UserResponse::from).toList(); }

    @Transactional
    UserResponse changeRole(UUID id, ChangeUserRoleRequest command) {
        AppUser user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.changeRole(command.role());
        activity.audit(id, "ROLE_CHANGED", "USER", id, "Role changed to " + command.role());
        activity.notify(id, "Your ProcureFlow role changed", "You are now assigned the " + command.role() + " role.");
        return UserResponse.from(user);
    }
}
