CREATE TABLE IF NOT EXISTS warehouse_products (
                                                  product_id UUID PRIMARY KEY,
                                                  fragile    BOOLEAN NOT NULL,
                                                  width      DOUBLE PRECISION NOT NULL CHECK (width >= 1),
                                                  height     DOUBLE PRECISION NOT NULL CHECK (height >= 1),
                                                  depth      DOUBLE PRECISION NOT NULL CHECK (depth >= 1),
                                                  weight     DOUBLE PRECISION NOT NULL CHECK (weight >= 1),
                                                  quantity   BIGINT NOT NULL DEFAULT 0 CHECK (quantity >= 0)
);
