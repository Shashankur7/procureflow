# Contributing to ProcureFlow

Thank you for your interest in improving ProcureFlow.

## Development workflow

1. Create a focused branch from `main`.
2. Make the smallest change that solves the problem.
3. Run the backend tests with `mvn --batch-mode test`.
4. Run the frontend production build with `cd frontend && npm ci && npm run build`.
5. Update documentation when behavior or setup changes.
6. Open a pull request with a clear description of the change and validation performed.

## Code expectations

- Keep business rules in the service/domain layer rather than controllers.
- Preserve role-based access checks and server-side authorization.
- Do not trust identity or authorization decisions supplied by the client.
- Keep database schema changes in Flyway migrations.
- Avoid committing credentials, tokens, local build output, or environment-specific files.

## Commit style

Prefer concise, imperative commit messages such as:

```text
Add purchase order validation
Improve inventory receipt tests
Document local deployment
```

## Pull requests

Include:

- What changed
- Why it changed
- How it was tested
- Any API, database, or UI impact
- Any documentation that should be updated
