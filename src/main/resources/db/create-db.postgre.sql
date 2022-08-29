CREATE SEQUENCE IF NOT EXISTS id_users_sequence START 1 INCREMENT 1;
CREATE SEQUENCE IF NOT EXISTS id_products_sequence START 1 INCREMENT 1;
CREATE SEQUENCE IF NOT EXISTS id_items_receipt_sequence START 1 INCREMENT 1;
CREATE SEQUENCE IF NOT EXISTS id_receipts_sequence START 1 INCREMENT 1;

CREATE TYPE user_roles AS ENUM (
    'admin',
    'cashier',
    'senior_cashier',
    'commodity expert'
);

CREATE TABLE IF NOT EXISTS users
(
    user_id  BIGINT DEFAULT nextval('id_users_sequence'),
    login    varchar(40) NOT NULL UNIQUE,
    password varchar(40) NOT NULL,
    role     USER_ROLES  NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS products
(
    product_id      BIGINT DEFAULT (nextval('id_products_sequence')),
    title_ukr       varchar(80) NOT NULL,
    title_eng       varchar(80) NOT NULL,
    price           FLOAT8      NOT NULL,
    code            varchar(80) NOT NULL UNIQUE,
    quantity        BIGINT      NOT NULL,
    date_creation   DATE,
    user_creator_id BIGINT,
    PRIMARY KEY (product_id),
    FOREIGN KEY (user_creator_id) REFERENCES users (user_id),
    CHECK ( price >= 0 AND quantity >= 0)
);

CREATE TABLE IF NOT EXISTS receipts
(
    receipt_id       BIGINT DEFAULT nextval('id_receipts_sequence'),
    total_price      FLOAT8 NOT NULL,
    user_creator_id  BIGINT,
    date_creation    DATE,
    isCanceled       BOOLEAN,
    user_canceler_id BIGINT,
    PRIMARY KEY (receipt_id),
    FOREIGN KEY (user_creator_id) REFERENCES users (user_id),
    FOREIGN KEY (user_canceler_id) REFERENCES users (user_id),
    CHECK ( total_price >= 0 )
);

CREATE TABLE IF NOT EXISTS items_receipt
(
    item_receipt_id BIGINT,
    product_id      BIGINT NOT NULL,
    isCanceled      BOOLEAN,
    canceler_id     BIGINT,
    PRIMARY KEY (item_receipt_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE IF NOT EXISTS receipts_products
(
    receipt_id      BIGINT,
    item_receipt_id BIGINT,
    PRIMARY KEY (receipt_id, item_receipt_id),
    FOREIGN KEY (receipt_id) REFERENCES receipts (receipt_id),
    FOREIGN KEY (item_receipt_id) REFERENCES items_receipt (item_receipt_id)
);