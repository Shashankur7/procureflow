# Deployment guide

## Local development

ProcureFlow is easiest to run locally with PostgreSQL in Docker, Spring Boot on port 8080, and Vite on port 5173.

### 1. Configure the database

Docker Compose provides development defaults, but you can override them with environment variables:

```text
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_PORT
```

The backend reads the corresponding `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD` values. If these are not supplied, the application uses the local development defaults documented in `application.yml`.

### 2. Start PostgreSQL

```bash
docker compose up -d
```

### 3. Start the backend

```bash
mvn spring-boot:run
```

### 4. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend is available at `http://localhost:5173` and proxies `/api` requests to the backend.

## Environment variables

For a deployment or shared development environment, configure these values outside Git:

| Variable | Purpose |
|---|---|
| `DATABASE_URL` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | Database account |
| `DATABASE_PASSWORD` | Database password |
| `JWT_SECRET` | JWT signing secret; use a strong secret stored in a secret manager |
| `JWT_EXPIRATION_MINUTES` | Access-token lifetime |
| `CORS_ALLOWED_ORIGIN` | Allowed frontend origin |
| `BOOTSTRAP_ADMIN_EMAIL` | Optional application bootstrap configuration |

Do not commit real passwords, JWT secrets, tokens, or production connection strings.

## Backend container

Build the backend image with:

```bash
docker build -t procureflow-api .
```

A production deployment should inject database and JWT configuration through the platform's secret/configuration mechanism.

## Production checklist

- Use managed PostgreSQL with backups and tested restore procedures.
- Store credentials and JWT secrets in a secret manager.
- Use HTTPS for all external traffic.
- Restrict CORS to the deployed frontend origin.
- Use a production database account with appropriate permissions.
- Keep Flyway migrations enabled and let Hibernate validate the schema.
- Build the React application with `npm run build` and serve the generated `dist/` output through a suitable web server/CDN.
- Keep GitHub Actions checks required before merging.
- Add production observability, rate limiting, and integration testing before handling real business data.

ProcureFlow is a portfolio project. A production security and operational review is required before real organizational or personal data is introduced.
