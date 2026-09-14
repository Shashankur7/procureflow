package com.procureflow.common.notification;

import com.procureflow.identity.UserPrincipal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final JdbcTemplate jdbc;
    public NotificationController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping
    List<NotificationResponse> mine(Authentication authentication) {
        UUID userId = ((UserPrincipal) authentication.getPrincipal()).id();
        return jdbc.query("select id, title, message, created_at, read_at from app_notifications where user_id = ? order by created_at desc limit 20", (rs, row) ->
                new NotificationResponse(UUID.fromString(rs.getString("id")), rs.getString("title"), rs.getString("message"),
                        rs.getTimestamp("created_at").toInstant(), rs.getTimestamp("read_at") != null), userId);
    }
}
