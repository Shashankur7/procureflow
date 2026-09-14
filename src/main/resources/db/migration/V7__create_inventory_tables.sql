CREATE TABLE products (
    id UUID PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(160) NOT NULL,
    reorder_level INTEGER NOT NULL DEFAULT 0 CHECK (reorder_level >= 0),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_levels (
    product_id UUID PRIMARY KEY REFERENCES products(id),
    quantity_on_hand INTEGER NOT NULL DEFAULT 0 CHECK (quantity_on_hand >= 0),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inventory_transactions (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    transaction_type VARCHAR(30) NOT NULL,
    quantity_delta INTEGER NOT NULL CHECK (quantity_delta <> 0),
    reference_note VARCHAR(500),
    performed_by UUID NOT NULL REFERENCES app_users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_inventory_transaction_type CHECK (transaction_type IN ('RECEIPT', 'ADJUSTMENT', 'ISSUE'))
);

CREATE INDEX idx_inventory_transactions_product_id ON inventory_transactions(product_id);
