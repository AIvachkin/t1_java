-- liquibase formatted sql

-- changeset aivachkin:9
INSERT INTO account (id, balance, client_id)
SELECT 1, 10000.00, 1
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 1);

INSERT INTO account (id, balance, client_id)
SELECT 2, 15000.50, 2
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 2);

INSERT INTO account (id, balance, client_id)
SELECT 3, 8500.25, 3
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 3);

INSERT INTO account (id, balance, client_id)
SELECT 4, 20000.00, 4
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 4);

INSERT INTO account (id, balance, client_id)
SELECT 5, 12750.75, 5
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 5);

INSERT INTO account (id, balance, client_id)
SELECT 6, 18000.00, 6
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 6);

INSERT INTO account (id, balance, client_id)
SELECT 7, 9800.50, 7
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 7);

INSERT INTO account (id, balance, client_id)
SELECT 8, 14500.00, 8
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 8);

INSERT INTO account (id, balance, client_id)
SELECT 9, 7200.25, 9
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 9);

INSERT INTO account (id, balance, client_id)
SELECT 10, 22000.00, 10
    WHERE NOT EXISTS (SELECT 1 FROM account WHERE id = 10);