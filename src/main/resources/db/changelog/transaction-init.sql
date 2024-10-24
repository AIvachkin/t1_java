-- liquibase formatted sql

-- changeset aivachkin:8
INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 1, 500.00, 1, 1
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 1);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 2, 2000.00, 2, 2
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 2);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 3, 150.75, 3, 3
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 3);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 4, 1800.00, 4, 4
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 4);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 5, 600.25, 5, 5
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 5);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 6, 1200.00, 6, 6
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 6);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 7, 350.50, 7, 7
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 7);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 8, 1000.00, 8, 8
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 8);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 9, 275.75, 9, 9
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 9);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 10, 2500.00, 10, 10
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 10);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 11, 800.00, 1, 1
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 11);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 12, 1500.00, 2, 2
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 12);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 13, 300.25, 3, 3
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 13);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 14, 2200.00, 4, 4
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 14);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 15, 450.75, 5, 5
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 15);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 16, 1800.50, 6, 6
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 16);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 17, 600.00, 7, 7
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 17);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 18, 1200.25, 8, 8
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 18);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 19, 400.00, 9, 9
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 19);

INSERT INTO transaction (id, amount, client_id, account_id)
SELECT 20, 3000.00, 10, 10
    WHERE NOT EXISTS (SELECT 1 FROM transaction WHERE id = 20);