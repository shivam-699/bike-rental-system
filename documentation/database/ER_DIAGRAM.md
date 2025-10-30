text
# Bike Rental System - Database ER Diagram & Schema Documentation

## 📊 Database Overview

The Bike Rental System uses a **MySQL database** with **11 normalized tables** implementing a complete rental management system.

## 🗄️ Entity Relationship Diagram
USERS (1) ←→ (M) RENTALS (M) ←→ (1) BIKES
↓ ↓
(M) MESSAGES (M) (1) FEEDBACK
↓
(M) TICKETS (1) ←→ (M) TICKET_REPLIES

Other Tables:

PROMO_CODES (1) ←→ (M) RENTALS

BROADCASTS (Standalone)

MAINTENANCE_LOG (M) ←→ (1) BIKES

PAYMENTS (1) ←→ (1) RENTALS

text

## 📋 Table Relationships & Descriptions

### **Core Entities**

#### 1. USERS Table
- **Primary Key**: `user_id`
- **Purpose**: Stores all system users (admins and customers)
- **Relationships**:
  - One-to-Many with RENTALS (One user can have multiple rentals)
  - One-to-Many with MESSAGES (User can send/receive multiple messages)
  - One-to-Many with TICKETS (User can create multiple support tickets)

#### 2. BIKES Table  
- **Primary Key**: `bike_id`
- **Purpose**: Manages bike inventory and availability
- **Relationships**:
  - One-to-Many with RENTALS (One bike can be rented multiple times)
  - One-to-Many with MAINTENANCE_LOG (One bike can have multiple maintenance records)

#### 3. RENTALS Table
- **Primary Key**: `rental_id`
- **Purpose**: Tracks all rental transactions
- **Foreign Keys**: `user_id`, `bike_id`, `promo_code`
- **Relationships**:
  - Many-to-One with USERS (Many rentals belong to one user)
  - Many-to-One with BIKES (Many rentals for one bike)
  - Many-to-One with PROMO_CODES (Many rentals can use one promo code)
  - One-to-One with PAYMENTS (One rental has one payment)

### **Supporting Entities**

#### 4. MESSAGES Table
- **Primary Key**: `msg_id`
- **Purpose**: Enables communication between users and admins
- **Foreign Keys**: `from_user`, `to_user`

#### 5. TICKETS & TICKET_REPLIES Tables
- **Purpose**: Support ticket management system
- **Relationship**: One-to-Many (One ticket can have multiple replies)

#### 6. PROMO_CODES Table
- **Primary Key**: `code`
- **Purpose**: Manages discount codes and promotions

## 🔑 Key Design Decisions

### **1. Normalization**
- All tables are in **3rd Normal Form (3NF)**
- No redundant data storage
- Proper foreign key relationships

### **2. User Role Management**
- Single USERS table with `role` enum ('admin', 'customer')
- Simplified authentication while maintaining security

### **3. Rental Status Workflow**
- Status progression: `pending` → `active` → `completed`/`rejected`
- Supports admin approval process

### **4. Communication System**
- Separate tables for messages and support tickets
- Allows different communication patterns

## 💾 Sample Complex Queries

### **1. Get User Rental History with Bike Details**
```sql
SELECT r.rental_id, u.name, b.brand, b.model, r.start_time, r.end_time, r.total_cost
FROM rentals r
JOIN users u ON r.user_id = u.user_id
JOIN bikes b ON r.bike_id = b.bike_id
WHERE u.user_id = ?
ORDER BY r.start_time DESC;
2. Calculate Monthly Revenue
sql
SELECT MONTH(start_time) as month, SUM(total_cost) as revenue
FROM rentals 
WHERE status = 'completed' AND YEAR(start_time) = YEAR(CURDATE())
GROUP BY MONTH(start_time);
3. Find Most Popular Bikes
sql
SELECT b.bike_id, b.brand, b.model, COUNT(r.rental_id) as rental_count
FROM bikes b
LEFT JOIN rentals r ON b.bike_id = r.bike_id
GROUP BY b.bike_id, b.brand, b.model
ORDER BY rental_count DESC;
🛠️ Database Setup
See /database/schema.sql for complete table creation scripts and /database/sample_data.sql for sample data.

text

### **STEP 3: Paste in Notepad**
1. **Press Ctrl+A** (select all)
2. **Press Delete** (clear everything)
3. **Press Ctrl+V** (paste the content)
4. **Press Ctrl+S** (save)
5. **Close Notepad**

### **STEP 4: Verify**
```cmd
type documentation\database\ER_DIAGRAM.md