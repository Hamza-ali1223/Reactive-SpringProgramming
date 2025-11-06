-- V2__populate_data.sql
-- Inserts initial seed data into 'book'

INSERT INTO book (book_name, book_author, book_publisher) VALUES
                                                              ('The Reactive Revolution', 'John Doe', 'Async Press'),
                                                              ('Spring in Action', 'Craig Walls', 'Manning'),
                                                              ('Java Concurrency in Practice', 'Brian Goetz', 'Addison-Wesley'),
                                                              ('Reactive Spring', 'Josh Long', 'O\'Reilly');
