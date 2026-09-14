# ProcureFlow

A role-based procurement and inventory workflow platform built with Java and React.

ProcureFlow models a complete internal purchasing flow: an employee submits a purchase request, a manager approves or rejects it, procurement creates a purchase order, and warehouse staff receive stock into inventory.

## Overview

ProcureFlow is implemented as a modular monolith with a React + Vite frontend, Spring Boot REST API, PostgreSQL database, Flyway migrations, JWT authentication, role-based authorization, Docker-based local infrastructure, OpenAPI documentation, automated tests, and GitHub Actions CI.

The project is designed to demonstrate practical backend engineering and full-stack development skills rather than a collection of isolated CRUD screens.

## Core workflow

```text
Employee -> Purchase Request -> Manager Approval -> Procurement -> Purchase Order -> Warehouse Receipt -> Inventory
```

## Role-based workspaces

| Role | Main responsibilities |
|---|---|
| Employee | Create purchase requests and review request history |
| Manager | Review pending requests and record approval decisions |
| Procurement | Manage suppliers, purchase orders, and the product catalogue |
| Warehouse | View inventory and record stock receipts |
| Admin | Manage user roles |

## Technical architecture

```text
React + Vite
     |
 HTTP / JSON + JWT
     v
Spring Boot REST API
 Controllers -> Services -> Repositories
     |
 JPA / JDBC + Flyway
     v
PostgreSQL
```

The modular-monolith approach keeps business modules separated while retaining a single deployable backend and straightforward transactional boundaries.

See [ARCHITECTURE.md](ARCHITECTURE.md) for the detailed design.

## Key engineering decisions

### Authentication and authorization

- Passwords are protected with BCrypt hashing.
- Authentication returns signed JWT access tokens.
- Backend authorization is role-based; frontend visibility is not treated as a security boundary.
- Purchase requests derive the requester from the authenticated identity rather than trusting a requester ID supplied by the client.

### Approval concurrency

Managers can decide only pending purchase requests. The decision path uses a database lock so two managers cannot successfully decide the same pending request at the same time. The decision is also recorded as an audit record.

### Purchase-order consistency

An approved request can produce only one purchase order. Purchase-order numbering uses a PostgreSQL sequence with the `PF-<year>-<sequence>` format, avoiding collisions during concurrent creation.

### Inventory traceability

Warehouse receipts update the current stock balance and append an immutable inventory transaction, providing both the operational quantity and its history.

### Database migrations

Flyway owns schema changes, while Hibernate validates the schema instead of silently changing it at application startup.

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
├── .github/workflows/       # Continuous integration
├── frontend/                # React + Vite web application
├── src/                     # Spring Boot backend
├── ARCHITECTURE.md          # Architecture and design decisions
├── DEMO.md                  # Five-minute product walkthrough
├── DEPLOYMENT.md            # Deployment and production checklist
├── INTERVIEW.md             # Interview discussion guide
├── Dockerfile               # Backend container image
├── docker-compose.yml       # Local PostgreSQL infrastructure
└── pom.xml                  # Maven build and dependencies
```

## Run locally

### Prerequisites

- Java 17+
- Maven 3.9+
- Node.js and npm
- Docker Desktop

### Start PostgreSQL

```bash
docker compose up -d
```

### Start the backend

```bash
mvn spring-boot:run
```

API health check: `http://localhost:8080/actuator/health`

### Start the frontend

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The Vite development server proxies `/api` requests to the Spring Boot API. See [frontend/README.md](frontend/README.md) for the frontend structure and role workspaces.

## API documentation

With the backend running, open `http://localhost:8080/swagger-ui/index.html` to inspect and test the REST API. Authenticate through the login endpoint and use **Authorize** with the returned JWT.

## Example API flow

```http
POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/purchase-requests
GET  /api/v1/purchase-requests/mine
```

The complete product flow is documented in [DEMO.md](DEMO.md).

## Testing and CI

Backend tests:

```bash
mvn --batch-mode test
```

Frontend production build:

```bash
cd frontend
npm ci
npm run build
```

GitHub Actions verifies the backend tests and frontend build on pushes to `main` and pull requests.

## Docker and deployment

The repository includes a backend `Dockerfile` and Docker Compose configuration for local PostgreSQL infrastructure.

For environment variables, secrets, HTTPS, database backups, and production deployment considerations, see [DEPLOYMENT.md](DEPLOYMENT.md).

## Documentation

- [Architecture](ARCHITECTURE.md) — modules, data flow, and design decisions
- [Demo](DEMO.md) — five-minute product walkthrough
- [Deployment](DEPLOYMENT.md) — deployment and production checklist
- [Interview guide](INTERVIEW.md) — engineering decisions to discuss in interviews
- [Frontend guide](frontend/README.md) — React application structure and role workspaces
- [Contributing](CONTRIBUTING.md) — development and pull-request workflow
- [Security policy](SECURITY.md) — vulnerability reporting and security principles

## Portfolio note

ProcureFlow is a portfolio project built to demonstrate full-stack Java engineering, secure REST APIs, relational data modelling, workflow design, concurrency control, database migrations, automated verification, and containerized development.

It should be security-reviewed and adapted before being used with real organizational or personal data.
