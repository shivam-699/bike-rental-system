# Bike Rental System - Technical Architecture

## 🏗️ System Architecture Overview

The Bike Rental System follows a **3-tier architecture** with clear separation of concerns:
┌─────────────────┐ ┌──────────────────┐ ┌─────────────────┐
│ PRESENTATION │ │ BUSINESS LOGIC │ │ DATA ACCESS │
│ LAYER │ │ LAYER │ │ LAYER │
├─────────────────┤ ├──────────────────┤ ├─────────────────┤
│ Java Swing │◄──►│ Controllers │◄──►│ MySQL Database │
│ Views │ │ (Business │ │ (11 Tables) │
│ │ │ Logic) │ │ │
└─────────────────┘ └──────────────────┘ └─────────────────┘

text

## 🛠️ Technology Stack

### **Frontend Layer**
- **Framework**: Java Swing
- **UI Components**: JFrame, JPanel, JTable, JList, JButton
- **Event Handling**: ActionListener, DocumentListener

### **Business Logic Layer**
- **Controllers**: BikeController, RentalController, UserController, etc.
- **Models**: POJO classes (User, Bike, Rental, etc.)
- **Utilities**: DatabaseConnection, Authentication

### **Data Access Layer**
- **Database**: MySQL 8.0
- **Connection**: JDBC Driver
- **ORM**: Custom DAO pattern

### **Build & Dependencies**
- **Build Tool**: Maven
- **Security**: BCrypt password hashing
- **Database Driver**: MySQL Connector/J

## 📁 Project Structure
src/main/java/bike/rental/system/
├── controller/ # Business logic controllers
│ ├── BikeController.java
│ ├── RentalController.java
│ ├── UserController.java
│ └── ...
├── model/ # Data models (POJOs)
│ ├── User.java
│ ├── Bike.java
│ ├── Rental.java
│ └── ...
├── view/ # UI components
│ ├── LoginView.java
│ ├── AdminDashboardView.java
│ ├── CustomerDashboardView.java
│ └── ...
└── util/ # Utilities
├── DatabaseConnection.java
└── GenerateHash.java

text

## 🔄 Data Flow

### **User Authentication Flow**
1. **LoginView** collects credentials
2. **UserController** validates against database
3. **BCrypt** verifies password hash
4. Redirect to appropriate dashboard

### **Bike Rental Flow**
1. **Customer** selects bike and duration
2. **RentalController** calculates cost with promo
3. **Wallet balance** checked and updated
4. **Rental record** created in database
5. **Bike status** updated to unavailable

### **Admin Management Flow**
1. **AdminDashboard** displays system overview
2. **Controllers** handle CRUD operations
3. **Real-time updates** across all modules
4. **Database transactions** ensure data consistency

## 🗄️ Database Integration

### **Connection Management**
- **Singleton pattern** for database connections
- **Connection pooling** for performance
- **Proper resource cleanup** with try-with-resources

### **Data Persistence**
- **CRUD operations** for all entities
- **Transaction management** for financial operations
- **Foreign key constraints** for data integrity

## 🔒 Security Features

### **Authentication**
- **BCrypt** password hashing
- **Session management** via user IDs
- **Role-based access control** (Admin/Customer)

### **Data Protection**
- **Parameterized queries** to prevent SQL injection
- **Input validation** on all user inputs
- **Secure password storage**

## 🚀 Deployment & Execution

### **Prerequisites**
- Java 8+
- MySQL 8.0+
- Maven 3.6+

### **Build & Run**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="bike.rental.system.view.LoginView"
📈 Scalability Considerations
Current Architecture Supports
Multiple concurrent users

Transactional integrity

Modular expansion

Future Enhancements
Web interface

Mobile application

Microservices architecture

Cloud deployment

text

## 🎯 **ACTION STEPS:**

1. **Run:** `notepad documentation\TECHNICAL_ARCHITECTURE.md`
2. **Copy-paste** the content above
3. **Save** and close
4. **Verify:** `type documentation\TECHNICAL_ARCHITECTURE.md`

## 💡 **What This Adds:**
- ✅ **Professional technical documentation**
- ✅ **System architecture overview**
- ✅ **Technology stack details**
- ✅ **Data flow explanations**
- ✅ **Security features**
- ✅ **Deployment instructions**

**Please create this file, then we'll create the final demonstration script!** 🚀