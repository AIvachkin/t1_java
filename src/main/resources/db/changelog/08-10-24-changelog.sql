-- liquibase formatted sql

-- changeset aivachkin:1
CREATE SEQUENCE IF NOT EXISTS time_limit_exceed_log_seq START WITH 1 INCREMENT BY 50;

-- changeset aivachkin:2
CREATE TABLE time_limit_exceed_log
(
    id                  BIGINT NOT NULL,
    method_signature    VARCHAR(255),
    execution_time      BIGINT,
    occurred_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_time_limit_exceed_log PRIMARY KEY (id)
);

-- changeset aivachkin:3
CREATE SEQUENCE IF NOT EXISTS data_source_error_log_seq START WITH 1 INCREMENT BY 50;

-- changeset aivachkin:4
CREATE TABLE data_source_error_log
(
    id                  BIGINT NOT NULL,
    method_signature    VARCHAR(255),
    error_message       TEXT,
    stack_trace         TEXT,
    occurred_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_data_source_error_log PRIMARY KEY (id)
);

-- changeset aivachkin:5
ALTER TABLE transaction
    ADD COLUMN account_id BIGINT;

-- changeset aivachkin:6
CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 50;

-- changeset aivachkin:7
CREATE TABLE account
(
    id                  BIGINT NOT NULL,
    balance             DECIMAL(19,2),
    client_id           BIGINT,
    CONSTRAINT pk_account PRIMARY KEY (id)
);