CREATE TABLE IF NOT EXISTS warehouse_products (
                                                  product_id UUID PRIMARY KEY,
                                                  fragile    BOOLEAN NOT NULL,
                                                  width      DOUBLE PRECISION NOT NULL CHECK (width >= 1),
                                                  height     DOUBLE PRECISION NOT NULL CHECK (height >= 1),
                                                  depth      DOUBLE PRECISION NOT NULL CHECK (depth >= 1),
                                                  weight     DOUBLE PRECISION NOT NULL CHECK (weight >= 1),
                                                  quantity   BIGINT NOT NULL DEFAULT 0 CHECK (quantity >= 0)
);

CREATE TABLE IF NOT EXISTS order_bookings (
                                              id         UUID PRIMARY KEY,
                                              order_id   UUID      NOT NULL UNIQUE,
                                              delivery_id UUID,
                                              created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_booking_products (
                                                      order_booking_id UUID    NOT NULL,
                                                      product_id       UUID    NOT NULL,
                                                      quantity         INTEGER NOT NULL,

                                                      CONSTRAINT pk_order_booking_products PRIMARY KEY (order_booking_id, product_id),
                                                      CONSTRAINT fk_order_booking_products_booking
                                                          FOREIGN KEY (order_booking_id)
                                                              REFERENCES order_bookings (id) ON DELETE CASCADE
);

