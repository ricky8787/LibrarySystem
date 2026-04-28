-- DDL for Library Borrowing System
CREATE DATABASE IF NOT EXISTS library;
USE library;

-- User table
CREATE TABLE IF NOT EXISTS User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_name VARCHAR(100),
    registration_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP NULL
);

-- Book table
CREATE TABLE IF NOT EXISTS Book (
    isbn VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    author VARCHAR(100),
    introduction TEXT
);

-- Inventory table
CREATE TABLE IF NOT EXISTS Inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL,
    store_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('在庫', '出借中', '整理中', '遺失', '損毀', '廢棄') DEFAULT '在庫',
    FOREIGN KEY (isbn) REFERENCES Book(isbn)
);

-- Borrowing Record table
CREATE TABLE IF NOT EXISTS Borrowing_Record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    inventory_id INT NOT NULL,
    borrowing_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_time TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (inventory_id) REFERENCES Inventory(inventory_id),
    INDEX idx_user_id (user_id),
    INDEX idx_inventory_id (inventory_id)
);

-- Stored Procedures

-- Register User
DELIMITER //
DROP PROCEDURE IF EXISTS sp_register_user //
CREATE PROCEDURE sp_register_user(
    IN p_phone VARCHAR(20),
    IN p_password VARCHAR(255),
    IN p_name VARCHAR(100),
    OUT p_result INT
)
BEGIN
    DECLARE v_count INT;
    SELECT COUNT(*) INTO v_count FROM User WHERE phone_number = p_phone;
    
    IF v_count > 0 THEN
        SET p_result = -1; -- Phone already exists
    ELSE
        INSERT INTO User (phone_number, password, user_name) VALUES (p_phone, p_password, p_name);
        SET p_result = 1; -- Success
    END IF;
END //
DELIMITER ;

-- Borrow Book with Transaction
DELIMITER //
DROP PROCEDURE IF EXISTS sp_borrow_book //
CREATE PROCEDURE sp_borrow_book(
    IN p_user_id INT,
    IN p_inventory_id INT,
    OUT p_result INT
)
BEGIN
    DECLARE v_status VARCHAR(20);
    
    -- Start Transaction
    START TRANSACTION;
    
    SELECT status INTO v_status FROM Inventory WHERE inventory_id = p_inventory_id FOR UPDATE;
    
    IF v_status = '在庫' THEN
        -- Update inventory status
        UPDATE Inventory SET status = '出借中' WHERE inventory_id = p_inventory_id;
        
        -- Insert borrowing record
        INSERT INTO Borrowing_Record (user_id, inventory_id, borrowing_time) 
        VALUES (p_user_id, p_inventory_id, CURRENT_TIMESTAMP);
        
        COMMIT;
        SET p_result = 1; -- Success
    ELSE
        ROLLBACK;
        SET p_result = -1; -- Book not available
    END IF;
END //
DELIMITER ;

-- Return Book with Transaction
DELIMITER //
DROP PROCEDURE IF EXISTS sp_return_book //
CREATE PROCEDURE sp_return_book(
    IN p_record_id INT,
    OUT p_result INT
)
BEGIN
    DECLARE v_inventory_id INT;
    DECLARE v_return_time TIMESTAMP;
    
    -- Start Transaction
    START TRANSACTION;
    
    SELECT inventory_id, return_time INTO v_inventory_id, v_return_time 
    FROM Borrowing_Record WHERE id = p_record_id FOR UPDATE;
    
    IF v_inventory_id IS NOT NULL AND v_return_time IS NULL THEN
        -- Update inventory status to '在庫'
        UPDATE Inventory SET status = '在庫' WHERE inventory_id = v_inventory_id;
        
        -- Update return time
        UPDATE Borrowing_Record SET return_time = CURRENT_TIMESTAMP WHERE id = p_record_id;
        
        COMMIT;
        SET p_result = 1; -- Success
    ELSE
        ROLLBACK;
        SET p_result = -1; -- Record not found or already returned
    END IF;
END //
DELIMITER ;
