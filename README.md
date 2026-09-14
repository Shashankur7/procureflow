# ProcureFlow

ProcureFlow is a portfolio-grade B2B procurement and inventory platform. Employees request purchases, managers approve them, procurement creates purchase orders, and warehouse staff receive stock.

## Why this project

It demonstrates secure Java REST APIs, relational modelling, business workflows, database migrations, automated tests, containers, and production health checks—the skills expected in modern Java roles.

## Architecture

The first version is a **modular monolith**. Each business area owns its controller, service, repository and domain model. This allows us to deliver a coherent application before later extracting event-driven components where justified.

## Current foundation

- Java 17 and Spring Boot 3
- PostgreSQL with Flyway schema migrations
- Spring Security boundary (health endpoints public; application endpoints protected)
- BCrypt password hashing and JWT login
- Docker Compose development database
- Actuator health and information endpoints

## Run locally

Prerequisites: Java 17+, Maven 3.9+, and Docker Desktop.

```bash
docker compose up -d
mvn spring-boot:run
```

Then open `http://localhost:8080/actuator/health`.

The project targets Java 17 so it runs on the installed Eclipse Temurin JDK. Restart IntelliJ's terminal after installing Maven or Docker so the updated PATH is detected.

## Run the frontend

Keep the API running in one terminal. In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The React application proxies `/api` calls to the Spring Boot API and has screens for authentication, purchase requests, and procurement tasks.

The current interface uses a professional application shell with role-specific workspaces for employees, managers, procurement, warehouse, and administration. See [frontend/README.md](frontend/README.md) for its screens and structure.

## API documentation

With the API running, open `http://localhost:8080/swagger-ui/index.html` to explore and test the REST API. Use the **Authorize** button to paste a JWT returned by login.

## First API workflow

`POST /api/v1/auth/register` creates an employee account and returns an access token. `POST /api/v1/auth/login` verifies a BCrypt password hash and returns a signed JWT. The purchase-request endpoints derive the requester from `Authorization: Bearer <token>`—they never trust a requester ID supplied in the request body.

`POST /api/v1/purchase-requests` creates a request in `PENDING_APPROVAL` status. `GET /api/v1/purchase-requests/mine` lists the signed-in employee's requests.

## Planned modules

1. Identity and role-based access
2. Purchase requests and approval workflow
3. Purchase orders and suppliers
4. Inventory and stock movement ledger
5. Audit history and notifications
6. React operations dashboard

## Interview explanation

“I chose a modular monolith for the initial product so transactions and delivery stay simple. I isolated business modules and introduced database migrations from day one. I would use asynchronous events only for independent work such as stock alerts and notifications, where the eventual-consistency trade-off is appropriate.”

## Approval workflow

Only a user with the `MANAGER` role can call `POST /api/v1/purchase-requests/{id}/decision`. The service uses a database lock while it changes a pending request to `APPROVED` or `REJECTED`, and stores an immutable decision record. The lock prevents two managers from deciding the same request at the same time.

## Purchase orders

Only `PROCUREMENT` users can create suppliers and create purchase orders. A purchase order can be created only once for an approved request. Its number uses a PostgreSQL sequence (`PF-<year>-<sequence>`), avoiding collisions when multiple procurement users create orders.

## Inventory

Procurement owns the product catalogue. `WAREHOUSE` users record received quantities through `POST /api/v1/inventory/receipts`. Every receipt updates the stock balance and appends an immutable inventory transaction, which is the basis for auditable stock history.

Read [ARCHITECTURE.md](ARCHITECTURE.md) for the module and workflow design, and [INTERVIEW.md](INTERVIEW.md) for a concise explanation you can practice.

Use [DEMO.md](DEMO.md) for a five-minute live interview walkthrough.

## Quality and delivery

`mvn test` runs backend business-rule tests. `npm run build` creates a production frontend build. GitHub Actions verifies both on each push. See [DEPLOYMENT.md](DEPLOYMENT.md) for deployment configuration and production safety notes.
