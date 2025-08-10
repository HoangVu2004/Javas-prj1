CREATE DATABASE IF NOT EXISTS help_support_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE help_support_db;

CREATE TABLE IF NOT EXISTS help (
    user_id VARCHAR(255) NOT NULL,
    lab_id VARCHAR(255) NOT NULL,
    support_count INT NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, lab_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
