INSERT INTO users(login, password, role)
VALUES ('Vlad', '1234', 'ADMIN'::user_roles);
INSERT INTO users(login, password, role)
VALUES ('Cashier', '1234', 'CASHIER'::user_roles);
INSERT INTO users(login, password, role)
VALUES ('Senior_Cashier', '1234', 'SENIOR_CASHIER'::user_roles);
INSERT INTO users(login, password, role)
VALUES ('Expert', '1234', 'COMMODITY_EXPERT'::user_roles);

INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_1', 'Product_1', 1000, 'Pro-duct-1', 3, to_date('10-10-2000', 'DD-MM-YYYY'), 1, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_2', 'Product_2', 2000, 'Pro-duct-2', 17, to_date('10-10-2000', 'DD-MM-YYYY'), 1, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_3', 'Product_3', 1000, 'Pro-duct-3', 50, to_date('10-10-2000', 'DD-MM-YYYY'), 4, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_4', 'Product_4', 4000, 'Pro-duct-4', 0, to_date('12-10-2022', 'DD-MM-YYYY'), 4, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_5', 'Product_5', 1000, 'Pro-duct-5', 130, to_date('10-10-2000', 'DD-MM-YYYY'), 1, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_6', 'Product_6', 6000, 'Pro-duct-6', 12, to_date('12-10-2022', 'DD-MM-YYYY'), 1, false);
INSERT INTO products(title_ukr, title_eng, price, code, quantity, date_creation, user_creator_id, is_deleted)
VALUES ('Предмет_7', 'Product_7', 1000, 'Pro-duct-7', 10, to_date('10-10-2000', 'DD-MM-YYYY'), 1, false);

INSERT INTO receipts(receipt_code, total_price, user_creator_id, date_creation, is_done)
VALUES ('DDDD-BBBB-DDDD-BBBB', 1000, 1, to_date('10-10-2000', 'DD-MM-YYYY'), true);
INSERT INTO receipts(receipt_code, total_price, user_creator_id, date_creation, is_done)
VALUES ('DDDD-BBBB-DDDD-BBBG', 1000, 1, to_date('10-10-2000', 'DD-MM-YYYY'), true);

INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (1, 12, 1, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (1, 142, 2, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (1, 5, 3, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (1, 6, 4, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (2, 31, 1, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (2, 42, 2, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (2, 5, 3, false);
INSERT INTO items_receipt(receipt_id, quantity, product_id, is_canceled)
VALUES (2, 6, 4, false);