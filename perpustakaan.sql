CREATE DATABASE perpustakaan;

CREATE TABLE users (
    id_user INT(11) PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'member') NOT NULL,
    phone_number VARCHAR(15),
    contact_info TEXT
);

CREATE TABLE books (
    id_book INT(11) PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publisher VARCHAR(100),
    publication_year INT,
    total_books INT NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE loans (
    id_loan INT(11) PRIMARY KEY AUTOINCREMENT,
    id_user INT(11) NOT NULL,
    book_id VARCHAR(50) NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE,
    is_returned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id_user),
    FOREIGN KEY (book_id) REFERENCES books(id_book)
);

CREATE TABLE penalties (
    penalty_id INT(11) PRIMARY KEY AUTOINCREMENT,
    loan_id INT(11) NOT NULL,
    penalty_amount DOUBLE NOT NULL,
    penalty_status BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (loan_id) REFERENCES loans(id_loan)
);
