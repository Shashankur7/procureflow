CREATE TABLE suppliers (
    id UUID PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    email VARCHAR(255) NOT NULL,
    tax_identifier VARCHAR(50) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE purchase_order_number_sequence START WITH 1001;

CREATE TABLE purchase_orders (
    id UUID PRIMARY KEY,
    po_number VARCHAR(30) NOT NULL UNIQUE,
    purchase_request_id UUID NOT NULL UNIQUE REFERENCES purchase_requests(id),
    supplier_id UUID NOT NULL REFERENCES suppliers(id),
    total_amount NUMERIC(14, 2) NOT NULL CHECK (total_amount > 0),
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_by UUID NOT NULL REFERENCES app_users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_purchase_order_status CHECK (status IN ('DRAFT', 'SENT', 'RECEIVED', 'CANCELLED'))
);
