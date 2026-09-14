CREATE TABLE purchase_request_decisions (
    id UUID PRIMARY KEY,
    purchase_request_id UUID NOT NULL REFERENCES purchase_requests(id),
    decided_by UUID NOT NULL REFERENCES app_users(id),
    decision VARCHAR(20) NOT NULL,
    comment VARCHAR(500),
    decided_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_decision CHECK (decision IN ('APPROVED', 'REJECTED'))
);

CREATE INDEX idx_purchase_request_decisions_request_id
    ON purchase_request_decisions(purchase_request_id);
