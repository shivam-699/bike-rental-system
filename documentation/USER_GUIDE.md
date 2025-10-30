# Bike Rental System - User Guide & Setup Instructions

## 🚀 Quick Start Guide

### **For End Users**
1. **Download** the application
2. **Ensure Java** is installed on your system
3. **Run the application** using the commands below
4. **Use default credentials** for testing

### **For Developers**
1. **Clone the repository**
2. **Set up MySQL database**
3. **Configure database connection**
4. **Build and run**

## 👥 User Roles & Access

### **Administrator**
- **Username**: admin@test.com
- **Password**: admin123
- **Access**: Full system control

### **Customer** 
- **Username**: customer@test.com
- **Password**: customer123
- **Access**: Bike rental and personal account

## 🛠️ System Requirements

### **Software Requirements**
- **Java**: JDK 8 or higher
- **Database**: MySQL 8.0 or higher
- **Build Tool**: Maven 3.6+

### **Hardware Requirements**
- **RAM**: 2GB minimum
- **Storage**: 100MB free space
- **Processor**: 1GHz or faster

## 📥 Installation & Setup

### **Step 1: Database Setup**
```sql
-- Create database
CREATE DATABASE bike_rental_system;

-- Run schema script
SOURCE database/schema.sql;

-- Add sample data (optional)
SOURCE database/sample_data.sql;