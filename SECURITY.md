# Security Policy

## Reporting a vulnerability

Please do not publish security vulnerabilities in a public issue.

For a sensitive report, contact the project owner through the contact information on the owner's GitHub profile and include:

- A short description of the issue
- Steps to reproduce
- Affected component or endpoint
- Potential impact
- Any suggested mitigation

Please avoid including passwords, JWTs, database credentials, or other secrets in a report.

## Security principles

ProcureFlow is designed around several application-security practices:

- Passwords are stored using BCrypt hashing.
- Authentication uses signed JWT access tokens.
- Authorization is enforced by backend roles rather than frontend visibility alone.
- Purchase-request ownership is derived from the authenticated identity.
- Secrets and environment-specific credentials should be supplied through environment variables or a secret manager.

This project is a portfolio application and should receive a production security review before use with real business or personal data.
