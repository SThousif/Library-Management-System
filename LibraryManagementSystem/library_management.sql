-- Library Management System - Database Setup Script
-- HOW TO RUN: mysql -u root -p < library_management.sql
-- Default Admin: username=admin | password=admin123

CREATE DATABASE IF NOT EXISTS library_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE library_management;

DROP TABLE IF EXISTS issued_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS admins;
DROP TABLE IF EXISTS reservations; -- Added just in case
DROP TABLE IF EXISTS users; -- Added just in case

CREATE TABLE admins (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE books (
    book_id       INT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    author        VARCHAR(100) NOT NULL,
    category      VARCHAR(100) NOT NULL,
    quantity      INT NOT NULL DEFAULT 1,
    available_qty INT NOT NULL DEFAULT 1,
    added_on      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE issued_books (
    issue_id     INT AUTO_INCREMENT PRIMARY KEY,
    student_id   VARCHAR(50)  NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    book_id      INT NOT NULL,
    issue_date   DATE NOT NULL,
    return_date  DATE DEFAULT NULL,
    status       ENUM('ISSUED','RETURNED') NOT NULL DEFAULT 'ISSUED',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_books_title   ON books(title);
CREATE INDEX idx_books_author  ON books(author);
CREATE INDEX idx_issued_status ON issued_books(status);

-- Default admin account
INSERT INTO admins (username, password) VALUES ('admin', 'admin123');

-- Sample books
INSERT INTO books (title, author, category, quantity, available_qty) VALUES
('Introduction to Algorithms',          'Thomas H. Cormen',     'Computer Science',      3, 3),
('Clean Code',                          'Robert C. Martin',     'Software Engineering',  2, 2),
('Database System Concepts',            'Abraham Silberschatz', 'Database',              4, 4),
('Operating System Concepts',           'Abraham Silberschatz', 'Operating Systems',     3, 3),
('Computer Networks',                   'Andrew S. Tanenbaum',  'Networking',            2, 2),
('The Pragmatic Programmer',            'David Thomas',         'Software Engineering',  2, 2),
('Design Patterns',                     'Gang of Four',         'Software Engineering',  1, 1),
('Discrete Mathematics',                'Kenneth Rosen',        'Mathematics',           5, 5),
('Data Structures Using C',             'Yedidyah Langsam',     'Computer Science',      3, 3),
('Artificial Intelligence',             'Stuart Russell',       'Artificial Intelligence',2, 2);

SELECT 'Database setup complete! Admin: admin/admin123' AS Status;
