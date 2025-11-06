-- V1__init_table.sql
-- Creates the table structure for 'book'

CREATE TABLE IF NOT EXISTS book (
                                    book_id INT AUTO_INCREMENT PRIMARY KEY,
                                    book_name VARCHAR(255) NOT NULL,
    book_author VARCHAR(255),
    book_publisher VARCHAR(255)
    );
