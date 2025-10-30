# Bike Rental System ???? 
 
A complete Java Swing-based Bike Rental Management System with MySQL database integration. 
 
## Features 
 
### ????? Admin Panel 
- User Management 
- Bike Inventory Management  
- Rental Approvals 
- Promo Code Management 
- Support Ticket System 
- Financial Overview 
- Broadcast Messaging 
 
### ?? Customer Panel 
- Bike Rental with Search 
- Promo Code Application 
- Rental History 
- Wallet Management 
- Support Tickets 
- Messaging System 
 
## Technology Stack 
- **Frontend**: Java Swing 
- **Backend**: Java 
- **Database**: MySQL 
- **Build Tool**: Maven 
- **Authentication**: BCrypt Password Hashing 
 
## Setup Instructions 
 
1. **Database Setup**: 
   ```sql 
   CREATE DATABASE bike_rental_system; 
   -- Run the schema.sql file in the database/ folder 
   ``` 
 
2. **Configuration**: 
   - Update `DatabaseConnection.java` with your MySQL credentials 
   - Default test users: 
     - Admin: admin@test.com / admin123 
     - Customer: customer@test.com / customer123 
 
3. **Run the Application**: 
   ```bash 
   mvn clean compile 
   mvn exec:java -Dexec.mainClass="bike.rental.system.view.LoginView" 
   ``` 
