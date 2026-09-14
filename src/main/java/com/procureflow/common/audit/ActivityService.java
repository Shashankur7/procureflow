package com.procureflow.common.audit;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivityService {
    private final JdbcTemplate jdbc;

    public ActivityService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void audit(UUID actorId, String eventType, String entityType, UUID entityId, String details) {
        jdbc.update("insert into audit_events(id, actor_id, event_type, entity_type, entity_id, details, created_at) values (?, ?, ?, ?, ?, ?, now())",
                UUID.randomUUID(), actorId, eventType, entityType, entityId, details);
    }

    public void notify(UUID userId, String title, String message) {
        jdbc.update("insert into app_notifications(id, user_id, title, message, created_at) values (?, ?, ?, ?, now())",
                UUID.randomUUID(), userId, title, message);
    }
}
