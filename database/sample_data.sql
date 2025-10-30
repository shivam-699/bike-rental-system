-- Sample Data for Bike Rental System 
USE bike_rental_system; 
 
-- Insert sample users (passwords are hashed with BCrypt) 
INSERT INTO users (name, email, password_hash, role, balance) VALUES 
('Admin User', 'admin@test.com', '$2a$10$Y8xNlRnZ/NO4j3u6OjRYF.AvS8qknUd1GiwRp4i19RsRrxvBKmuDq', 'admin', 0.00), 
('Customer User', 'customer@test.com', '$2a$10$fqje88523hGWwX2AapPeVuJ6arxX7mvRFCcqZGEAyOuDM2lLtEToe', 'customer', 1000.00); 
 
-- Insert sample bikes 
INSERT INTO bikes (brand, model, price_per_hour, status, condition_description) VALUES 
('Hero', 'Sprint', 50.00, 'available', 'Good condition, regularly serviced'), 
('Atlas', 'Trailblazer', 75.00, 'available', 'New tires, excellent brakes'), 
('Hercules', 'MTB', 100.00, 'available', 'Mountain bike, 21 gears'), 
('BSA', 'Champion', 60.00, 'maintenance', 'Needs chain replacement'); 
 
-- Insert promo codes 
INSERT INTO promo_codes (code, discount_percent, valid_from, valid_to, uses_left) VALUES 
('WELCOME10', 10.00, '2024-01-01', '2024-12-31', 100), 
('SUMMER25', 25.00, '2024-06-01', '2024-08-31', 50); 
