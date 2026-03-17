CREATE TABLE optimisation_record
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    max_volume INT         NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE shipment
(
    id         UUID PRIMARY KEY        DEFAULT gen_random_uuid(),
    request_id UUID           NOT NULL REFERENCES optimisation_record (id),
    name       VARCHAR(255)   NOT NULL,
    volume     INT            NOT NULL,
    revenue    NUMERIC(12, 2) NOT NULL,
    selected   BOOLEAN        NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_shipment_request_id ON shipment (request_id);