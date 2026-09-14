# ProcureFlow

**Role-based procurement and inventory workflow platform built with Java, Spring Boot, PostgreSQL, and React.**

ProcureFlow models an internal purchasing workflow from request to inventory receipt:

```text
Employee → Purchase Request → Manager Approval → Purchase Order → Warehouse Receipt → Inventory
```

The project is structured as a modular monolith and focuses on practical backend engineering, secure REST APIs, relational data modelling, workflow rules, concurrency control, testing, and full-stack integration.

## Why this project

Procurement workflows are a good example of software that is more than CRUD. A request can require approval, an approved request must not create duplicate purchase orders, and received stock needs both a current balance and an auditable history.

ProcureFlow was built to explore those application-level rules in a single full-stack system.

## Features

- JWT authentication with BCrypt password hashing
- Backend-enforced role-based authorization
- Employee purchase-request creation and history
- Manager approval/rejection workflow with audit records
- Supplier and product catalogue management
- Purchase-order creation from approved requests
- PostgreSQL sequence-based purchase-order numbering
- Warehouse stock-receipt workflow
- Inventory balance and transaction history
- Flyway-managed database migrations
- Bean Validation and REST APIs
- Swagger/OpenAPI documentation
- React role-aware workspaces
- Docker Compose PostgreSQL development environment
- Automated backend tests and frontend production build in GitHub Actions

## Role-based workspaces

| Role | Responsibilities |
|---|---|
| Employee | Create purchase requests and review request history |
| Manager | Review pending requests and record approval decisions |
| Procurement | Manage suppliers, purchase orders, and products |
| Warehouse | View inventory and record stock receipts |
| Admin | Manage user roles |

## Architecture

```text
┌──────────────────────────────┐
│       React + Vite           │
│   Role-aware web workspace   │
└──────────────┬───────────────┘
               │ HTTP / JSON + JWT
               ▼
┌──────────────────────────────┐
│       Spring Boot API        │
│ Controllers → Services       │
│             → Repositories   │
└──────────────┬───────────────┘
               │ JPA / JDBC
               ▼
┌──────────────────────────────┐
│ PostgreSQL + Flyway          │
│ Schema + workflow data       │
└──────────────────────────────┘
```

The modular-monolith approach keeps business modules separated while retaining one deployable backend and straightforward transaction boundaries.

See [ARCHITECTURE.md](ARCHITECTURE.md) for the detailed design decisions.

## Engineering highlights

### Authentication and authorization

- Passwords are hashed with BCrypt.
- Login produces signed JWT access tokens.
- Authorization is enforced on the backend; hiding a screen in React is not treated as a security boundary.
- Purchase-request ownership is derived from the authenticated identity rather than trusting a requester ID from the client.

### Approval concurrency

Managers can decide only pending purchase requests. The decision path uses a database lock so concurrent managers cannot both successfully decide the same pending request. The decision is also recorded as an audit entry.

### Purchase-order consistency

An approved request can produce only one purchase order. Purchase-order numbering uses a PostgreSQL sequence with the `PF-<year>-<sequence>` format to avoid collisions during concurrent creation.

### Inventory traceability

Warehouse receipts update the current stock balance and append an immutable inventory transaction, providing both operational quantity and history.

### Database migrations

Flyway owns schema changes while Hibernate validates the schema instead of silently modifying it at application startup.

## Technology stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5 |
| REST API | Spring MVC, Bean Validation |
| Security | Spring Security, JWT, BCrypt |
| Persistence | Spring Data JPA, PostgreSQL |
| Database migration | Flyway |
| API documentation | Springdoc OpenAPI / Swagger UI |
| Frontend | React 19, Vite 7 |
| Infrastructure | Docker, Docker Compose |
| CI | GitHub Actions |

## Project structure

```text
procureflow/
├── .github/workflows/       # CI verification
├── frontend/                # React + Vite application
├── src/                     # Spring Boot backend
├── ARCHITECTURE.md          # Architecture and design decisions
├── DEMO.md                  # Five-minute walkthrough
├── DEPLOYMENT.md            # Environment and deployment guidance
├── INTERVIEW.md             # Interview discussion guide
├── Dockerfile               # Backend container image
├── docker-compose.yml       # Local PostgreSQL infrastructure
└── pom.xml                  # Maven configuration
```

## Run locally

### Prerequisites

- Java 17+
- Maven 3.9+
- Node.js and npm
- Docker Desktop

### 1. Start PostgreSQL

```bash
docker compose up -d
```

The Compose file provides development defaults and supports environment-variable overrides for database configuration.

### 2. Start the backend

```bash
mvn spring-boot:run
```

Health endpoint: `http://localhost:8080/actuator/health`

### 3. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The Vite development server proxies `/api` requests to the Spring Boot API.

## API documentation

With the backend running, open:

`http://localhost:8080/swagger-ui/index.html`

Use the login endpoint to obtain a JWT and the Swagger **Authorize** control to test protected endpoints.

Example API flow:

```http
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/purchase-requests
GET  /api/v1/purchase-requests/mine
```

See [DEMO.md](DEMO.md) for the complete workflow.

## Testing and CI

Run backend tests:

```bash
mvn --batch-mode test
```

Build the frontend:

```bash
cd frontend
npm ci
npm run build
```

GitHub Actions runs the backend test suite and frontend production build for pushes to `main` and pull requests.

The repository contains focused unit tests for core workflow logic, including purchase-request decisions. Additional integration coverage is a useful next step.

## Security and configuration

Development credentials are intentionally supplied through local configuration. Production credentials must be injected through environment variables or a secret manager.

See [SECURITY.md](SECURITY.md) and [DEPLOYMENT.md](DEPLOYMENT.md) before deploying the application.

## Documentation

- [Architecture](ARCHITECTURE.md) — modules, data flow, and design decisions
- [Demo](DEMO.md) — five-minute product walkthrough
- [Deployment](DEPLOYMENT.md) — environment and deployment guidance
- [Interview guide](INTERVIEW.md) — engineering decisions to discuss in interviews
- [Frontend guide](frontend/README.md) — React application structure and role workspaces
- [Contributing](CONTRIBUTING.md) — development and pull-request workflow
- [Security policy](SECURITY.md) — vulnerability reporting and security principles

## Portfolio status

**Portfolio project — actively being refined.**

ProcureFlow is intended to demonstrate full-stack Java engineering through a realistic business workflow rather than a collection of disconnected CRUD screens.

Before production use, the project should receive a dedicated security, integration-testing, observability, and deployment review.
