-- DML for Library Borrowing System
USE library;

-- Initial Books
-- 插入 6 本書
INSERT INTO Book (isbn, name, author, introduction) VALUES 
('9780132350884', 'Clean Code', 'Robert C. Martin', 'A Handbook of Agile Software Craftsmanship'),
('9780201616224', 'The Pragmatic Programmer', 'Andrew Hunt', 'From Journeyman to Master'),
('9780201633610', 'Design Patterns', 'Erich Gamma', 'Elements of Reusable Object-Oriented Software'),
('9780134757599', 'Refactoring', 'Martin Fowler', 'Improving the Design of Existing Code'),
('9780137081073', 'The Clean Coder', 'Robert C. Martin', 'A Code of Conduct for Professional Programmers'),
('9780596007126', 'Head First Design Patterns', 'Eric Freeman', 'A Brain-Friendly Guide');

-- Initial Inventory
INSERT INTO Inventory (isbn, status, store_time) VALUES 
('9780132350884', '在庫', '2023-10-01 10:00:00'),
('9780201616224', '在庫', '2023-11-15 14:30:00'),
('9780201633610', '在庫', '2024-01-20 09:15:00'),
('9780134757599', '在庫', '2022-03-01 11:15:00'),
('9780137081073', '在庫', '2021-07-15 23:35:00'),
('9780596007126', '在庫', '2022-11-20 12:00:00');
