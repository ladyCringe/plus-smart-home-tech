CREATE TABLE IF NOT EXISTS shopping_carts (
                                              shopping_cart_id UUID  PRIMARY KEY,
                                              username         VARCHAR NOT NULL,
                                              active           BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS shopping_cart_items (

                                                   shopping_cart_id UUID  NOT NULL REFERENCES shopping_carts (shopping_cart_id) ON DELETE CASCADE,
                                                   product_id       UUID  NOT NULL,
                                                   quantity         BIGINT      NOT NULL CHECK (quantity >= 0)
);

CREATE INDEX IF NOT EXISTS idx_shopping_carts_username_active
    ON shopping_carts (username, active);

CREATE INDEX IF NOT EXISTS idx_shopping_cart_items_cart
    ON shopping_cart_items (shopping_cart_id);

CREATE INDEX IF NOT EXISTS idx_shopping_cart_items_product
    ON shopping_cart_items (product_id);