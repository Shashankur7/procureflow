# ProcureFlow architecture

## Business workflow

```text
Employee request → Manager decision → Procurement purchase order → Warehouse receipt → Inventory balance
```

## Technical architecture

```text
React + Vite (localhost:5173)
          │ HTTP / JSON / JWT
Spring Boot API (localhost:8080)
          │ JPA + JDBC + Flyway
PostgreSQL (Docker)
```

## Modules

| Module | Responsibility |
|---|---|
| identity | Registration, BCrypt passwords, signed JWTs, roles |
| purchaserequest | Request creation and manager decision audit |
| supplier | Approved vendor records |
| purchaseorder | Converts an approved request into one unique PO |
| inventory | Products, stock balances, immutable receipt ledger |

## Design decisions

- A modular monolith keeps deployment and transactions simple while the product is small.
- Flyway owns the database schema; Hibernate validates rather than silently changing it.
- Managers lock a request before deciding it, preventing duplicate decisions.
- Purchase-order numbers use a PostgreSQL sequence, so concurrent creation cannot collide.
- Stock receipts create both an updated balance and an immutable transaction record.
