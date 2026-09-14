# ProcureFlow live demo script

## Start

1. Run PostgreSQL: `docker compose up -d`
2. Run the API: `mvn spring-boot:run`
3. Run the web app: `cd frontend && npm.cmd run dev`
4. Open `http://localhost:5173`

## Five-minute product walkthrough

1. Register an **Employee** and submit a purchase request for a laptop.
2. Sign in as a **Manager**, open **Approval queue**, and approve the request with a short comment.
3. Sign in as **Procurement**, add a supplier, select the approved request and supplier, and create a purchase order.
4. Add a product to the inventory catalogue.
5. Sign in as **Warehouse**, record a stock receipt and show the quantity increasing on the dashboard.
6. Sign in as **Admin**, use **User administration** to assign the next role instead of editing the database.

## What to say while demonstrating

- “Each screen is protected by the role embedded in the signed JWT.”
- “Managers can only decide pending requests; the decision is stored as an audit record.”
- “A database constraint and a sequence protect purchase-order consistency.”
- “Receiving stock updates the current balance and adds an immutable ledger entry.”

## Screenshots to add before publishing

- Login page
- Employee request dashboard
- Manager approval queue
- Procurement supplier and purchase-order workspace
- Warehouse inventory screen
- Swagger API page
