# Deployment guide

## Local development

Use Docker Compose for PostgreSQL, run Spring Boot on port 8080, and run Vite on port 5173. This is the recommended learning setup.

## Container API

Build the backend container after adding a Maven wrapper (or use a CI-built JAR image):

```bash
docker build -t procureflow-api .
```

Provide these environment variables in a real deployment:

| Variable | Purpose |
|---|---|
| `DATABASE_URL` | PostgreSQL JDBC URL |
| `DATABASE_USERNAME` | Database account |
| `DATABASE_PASSWORD` | Database password |
| `JWT_SECRET` | 32+ character signing secret stored in a secret manager |
| `CORS_ALLOWED_ORIGIN` | Public frontend URL |

## Production checklist

- Use a managed PostgreSQL service and daily backups.
- Store secrets in the cloud provider secret manager, never Git.
- Terminate HTTPS at a reverse proxy or cloud load balancer.
- Build the React app with `npm run build` and serve its `dist/` directory through a CDN or web server.
- Use GitHub Actions checks before merges.
