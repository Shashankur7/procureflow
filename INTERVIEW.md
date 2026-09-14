# Interview guide

## 60-second explanation

“ProcureFlow is a full-stack B2B procurement and inventory platform. Employees submit purchase requests, managers approve or reject them, procurement creates suppliers and purchase orders, and warehouse users record received stock. I built a React frontend and a Java 17 Spring Boot API, protected it with JWT role-based authorization, used PostgreSQL with Flyway migrations, and containerized the database with Docker Compose.”

## Questions to prepare

**Why modular monolith instead of microservices?**  A single deployable application makes the first product reliable and easy to debug. The modules have clear boundaries, so independent parts such as notifications can later be extracted when scale justifies it.

**How is it secured?**  Passwords are BCrypt hashes. Login returns a signed JWT containing the user ID and role. Spring Security checks the role at each privileged endpoint.

**How do you protect data integrity?**  Database constraints prevent invalid statuses and duplicate purchase orders. Flyway makes every schema change versioned. The approval flow locks the row during a decision.

**What would you add for production?**  Multiple roles per user, refresh tokens, API rate limiting, email notifications, observability, Testcontainers integration tests, CI/CD, and cloud deployment.
