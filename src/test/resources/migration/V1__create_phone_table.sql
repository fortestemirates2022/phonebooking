CREATE TABLE phone (
                       id IDENTITY NOT NULL PRIMARY KEY,
                       brand VARCHAR(255) NOT NULL,
                       model VARCHAR(255) NOT NULL,
                       description TEXT,
                       booked BOOLEAN NOT NULL DEFAULT false,
                       booked_by VARCHAR(255),
                       booked_at TIMESTAMP
);
INSERT INTO phone (brand, model, description)
VALUES
( 'Samsung', 'Galaxy S9', 'Samsung Galaxy S9 for testing purposes'),
('Samsung', 'Galaxy S8', 'Samsung Galaxy S8 for testing purposes'),
('Samsung', 'Galaxy S8', 'Samsung Galaxy S8 for testing purposes'),
('Motorola', 'Nexus 6', 'Motorola Nexus 6 for testing purposes'),
('Oneplus', '9', 'Oneplus 9 for testing purposes'),
('Apple', 'iPhone 13', 'Apple iPhone 13 for testing purposes'),
('Apple', 'iPhone 12', 'Apple iPhone 12 for testing purposes'),
('Apple', 'iPhone 11', 'Apple iPhone 11 for testing purposes'),
('Apple', 'iPhone X', 'Apple iPhone X for testing purposes'),
('Nokia', '3310', 'Nokia 3310 for testing purposes');