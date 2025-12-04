CREATE TABLE IF NOT EXISTS orders (
                                      order_id         UUID PRIMARY KEY,
                                      username         VARCHAR(100)        NOT NULL,
                                      shopping_cart_id UUID,
                                      order_state      VARCHAR(50)         NOT NULL,
                                      payment_id       UUID,
                                      delivery_id      UUID,

                                      delivery_weight  DOUBLE PRECISION,
                                      delivery_volume  DOUBLE PRECISION,
                                      fragile          BOOLEAN,

                                      country          VARCHAR(255),
                                      city             VARCHAR(255),
                                      street           VARCHAR(255),
                                      house            VARCHAR(50),
                                      flat             VARCHAR(50),

                                      total_price      NUMERIC(19, 2),
                                      delivery_price   NUMERIC(19, 2),
                                      product_price    NUMERIC(19, 2),

                                      created_at       TIMESTAMP,
                                      updated_at       TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_products (
                                              order_id   UUID NOT NULL,
                                              product_id UUID NOT NULL,
                                              quantity   INTEGER NOT NULL,

                                              CONSTRAINT pk_order_products PRIMARY KEY (order_id, product_id),
                                              CONSTRAINT fk_order_products_order
                                                  FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE
);
