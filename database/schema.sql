-- Bike Rental System Database Schema 
-- Run this SQL script in your MySQL database 
 
CREATE DATABASE IF NOT EXISTS bike_rental_system; 
USE bike_rental_system; 
 
-- Users table 
CREATE TABLE IF NOT EXISTS users ( 
    user_id INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(100) NOT NULL, 
    email VARCHAR(100) UNIQUE NOT NULL, 
    password_hash VARCHAR(255) NOT NULL, 
    role ENUM('admin', 'customer') DEFAULT 'customer', 
    balance DECIMAL(10,2) DEFAULT 0.00, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
); 
 
-- Bikes table 
CREATE TABLE IF NOT EXISTS bikes ( 
    bike_id INT AUTO_INCREMENT PRIMARY KEY, 
    brand VARCHAR(50) NOT NULL, 
    model VARCHAR(50) NOT NULL, 
    price_per_hour DECIMAL(8,2) NOT NULL, 
    status ENUM('available', 'unavailable', 'maintenance') DEFAULT 'available', 
    condition_description TEXT, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
); 
 
-- Rentals table 
CREATE TABLE IF NOT EXISTS rentals ( 
    rental_id INT AUTO_INCREMENT PRIMARY KEY, 
    user_id INT NOT NULL, 
    bike_id INT NOT NULL, 
    start_time DATETIME NOT NULL, 
    end_time DATETIME NOT NULL, 
    total_cost DECIMAL(10,2) NOT NULL, 
    promo_code VARCHAR(20), 
    status ENUM('pending', 'active', 'completed', 'rejected') DEFAULT 'pending', 
    fine_amount DECIMAL(8,2) DEFAULT 0.00, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    FOREIGN KEY (user_id) REFERENCES users(user_id), 
    FOREIGN KEY (bike_id) REFERENCES bikes(bike_id) 
); 
 
-- Messages table 
CREATE TABLE IF NOT EXISTS messages ( 
    msg_id INT AUTO_INCREMENT PRIMARY KEY, 
    from_user INT NOT NULL, 
    to_user INT NOT NULL, 
    text TEXT NOT NULL, 
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    is_read BOOLEAN DEFAULT FALSE, 
    FOREIGN KEY (from_user) REFERENCES users(user_id), 
    FOREIGN KEY (to_user) REFERENCES users(user_id) 
); 
 
