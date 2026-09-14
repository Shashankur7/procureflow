ALTER TABLE app_users ADD COLUMN password_hash VARCHAR(255);

-- This project has no production users yet. New registrations always supply a BCrypt hash.
ALTER TABLE app_users ALTER COLUMN password_hash SET NOT NULL;
