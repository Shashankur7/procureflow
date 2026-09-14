# ProcureFlow web application

The web client is a React + Vite single-page application with role-aware workspaces.

## Workspaces

| Role | Screens |
|---|---|
| Employee | Overview, purchase-request creation and history |
| Manager | Overview, approval queue and decision notes |
| Procurement | Supplier creation, purchase-order workflow, product catalogue |
| Warehouse | Inventory view and stock receipt workflow |
| Admin | User role administration |

## Frontend structure

```text
src/
  App.jsx          Application shell and workspace screens
  api.js           Authenticated API client
  main.jsx         React entry point
  workspace.css    Responsive application-shell styles
```

## Run

```powershell
npm.cmd run dev
```

The Vite dev server proxies `/api` to the Spring Boot API at port 8080.
