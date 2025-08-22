CREATE TABLE cart
(
    id         UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at TIMESTAMP                      NOT NULL,
    updated_at TIMESTAMP                      NOT NULL,
    user_id    UUID DEFAULT gen_random_uuid() NOT NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

CREATE TABLE cart_item
(
    id         UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at TIMESTAMP                      NOT NULL,
    updated_at TIMESTAMP                      NOT NULL,
    cart_id    UUID DEFAULT gen_random_uuid() NOT NULL,
    product_id UUID DEFAULT gen_random_uuid() NOT NULL,
    quantity   INT                            NOT NULL,
    CONSTRAINT pk_cart_item PRIMARY KEY (id)
);

CREATE TABLE category
(
    id          UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at  TIMESTAMP                      NOT NULL,
    updated_at  TIMESTAMP                      NOT NULL,
    name        VARCHAR(255)                   NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE invoice
(
    id                     UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at             TIMESTAMP                      NOT NULL,
    updated_at             TIMESTAMP                      NOT NULL,
    payment_id             UUID DEFAULT gen_random_uuid() NOT NULL,
    customer_name          VARCHAR(255)                   NOT NULL,
    customer_email         VARCHAR(255),
    external_id            VARCHAR(255)                   NOT NULL,
    invoice_url            VARCHAR(255),
    expiry_date            TIMESTAMP,
    status                 VARCHAR(255),
    invoice_pdf            VARCHAR(255),
    total_amount           DECIMAL                        NOT NULL,
    issued_date            TIMESTAMP,
    xendit_invoice_payload CLOB,
    CONSTRAINT pk_invoice PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id          UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at  TIMESTAMP                      NOT NULL,
    updated_at  TIMESTAMP                      NOT NULL,
    order_id    UUID DEFAULT gen_random_uuid(),
    product_id  UUID DEFAULT gen_random_uuid() NOT NULL,
    quantity    INT                            NOT NULL,
    total_price DECIMAL                        NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id          UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at  TIMESTAMP                      NOT NULL,
    updated_at  TIMESTAMP                      NOT NULL,
    user_id     UUID DEFAULT gen_random_uuid() NOT NULL,
    status      VARCHAR(255)                   NOT NULL,
    total_price DECIMAL                        NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE payment
(
    id             UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at     TIMESTAMP                      NOT NULL,
    updated_at     TIMESTAMP                      NOT NULL,
    order_id       UUID DEFAULT gen_random_uuid() NOT NULL,
    invoice_id     UUID DEFAULT gen_random_uuid(),
    payment_method VARCHAR(255),
    paid_at        TIMESTAMP,
    total_amount   DECIMAL                        NOT NULL,
    status         VARCHAR(255),
    CONSTRAINT pk_payment PRIMARY KEY (id)
);

CREATE TABLE product
(
    id             UUID DEFAULT gen_random_uuid() NOT NULL,
    created_at     TIMESTAMP                      NOT NULL,
    updated_at     TIMESTAMP                      NOT NULL,
    name           VARCHAR(255)                   NOT NULL,
    description    VARCHAR(255)                   NOT NULL,
    price          DECIMAL                        NOT NULL,
    stock_quantity INT                            NOT NULL,
    image_url      VARCHAR(255),
    category_id    UUID DEFAULT gen_random_uuid() NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);

CREATE TABLE session
(
    id                       UUID    DEFAULT gen_random_uuid() NOT NULL,
    created_at               TIMESTAMP                         NOT NULL,
    updated_at               TIMESTAMP                         NOT NULL,
    user_id                  UUID    DEFAULT gen_random_uuid() NOT NULL,
    access_token             VARCHAR(255)                      NOT NULL,
    refresh_token            VARCHAR(255)                      NOT NULL,
    is_session_revoked       BOOLEAN DEFAULT FALSE             NOT NULL,
    access_token_expires_at  TIMESTAMP,
    refresh_token_expires_at TIMESTAMP,
    session_revoked_at       TIMESTAMP,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID    DEFAULT gen_random_uuid() NOT NULL,
    created_at    TIMESTAMP                         NOT NULL,
    updated_at    TIMESTAMP                         NOT NULL,
    username      VARCHAR(255)                      NOT NULL,
    password      VARCHAR(255)                      NOT NULL,
    email         VARCHAR(255)                      NOT NULL,
    full_name     VARCHAR(255)                      NOT NULL,
    profile_image CLOB,
    enabled       BOOLEAN DEFAULT FALSE             NOT NULL,
    is_verified   BOOLEAN DEFAULT FALSE             NOT NULL,
    role          VARCHAR(255)                      NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE cart
    ADD CONSTRAINT uc_cart_user UNIQUE (user_id);

ALTER TABLE category
    ADD CONSTRAINT uc_category_name UNIQUE (name);

ALTER TABLE invoice
    ADD CONSTRAINT uc_invoice_external UNIQUE (external_id);

ALTER TABLE invoice
    ADD CONSTRAINT uc_invoice_payment UNIQUE (payment_id);

ALTER TABLE payment
    ADD CONSTRAINT uc_payment_invoice UNIQUE (invoice_id);

ALTER TABLE payment
    ADD CONSTRAINT uc_payment_order UNIQUE (order_id);

ALTER TABLE session
    ADD CONSTRAINT uc_session_refresh_token UNIQUE (refresh_token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CART_ITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE invoice
    ADD CONSTRAINT FK_INVOICE_ON_PAYMENT FOREIGN KEY (payment_id) REFERENCES payment (id);

ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE order_item
    ADD CONSTRAINT FK_ORDER_ITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE payment
    ADD CONSTRAINT FK_PAYMENT_ON_INVOICE FOREIGN KEY (invoice_id) REFERENCES invoice (id);

ALTER TABLE payment
    ADD CONSTRAINT FK_PAYMENT_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);

ALTER TABLE product
    ADD CONSTRAINT FK_PRODUCT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);