markdown
# Bike Rental System - Technical Documentation

## 🏗️ System Architecture

### **Architecture Overview**
Presentation Layer (Java Swing)
↓
Business Logic Layer (Controllers)
↓
Data Access Layer (Models)
↓
Database Layer (MySQL)

text

### **Technology Stack**
- **Frontend**: Java Swing GUI
- **Business Logic**: Java Core
- **Database**: MySQL 8.0+
- **Authentication**: BCrypt Password Hashing
- **Build Tool**: Maven
- **Version Control**: Git/GitHub

## 🗃️ Database Design

### **Entity Relationship Diagram**
Users ←---→ Rentals ←---→ Bikes
↓ ↓ ↓
Messages Payments Maintenance
↓
Tickets ←---→ TicketReplies
↓
Broadcasts

text

### **Core Tables Description**
1. **users** - User accounts and authentication
2. **bikes** - Bike inventory and specifications
3. **rentals** - Rental transactions and history
4. **messages** - User-to-user communication
5. **tickets** - Customer support system
6. **promo_codes** - Discount management

## 🔐 Security Implementation

### **Authentication System**
- BCrypt password hashing
- Role-based access control (Admin/Customer)
- Session management
- Input validation and sanitization

### **Data Protection**
- Password hashing before storage
- SQL injection prevention using PreparedStatement
- Input validation on all user inputs
- Role-based data access

## 💻 Code Structure

### **Package Organization**
bike.rental.system/
├── controller/ # Business logic
├── model/ # Data entities
├── util/ # Utilities
└── view/ # UI components

text

### **Key Classes & Responsibilities**

#### **Model Classes**
- `User.java` - User entity with authentication
- `Bike.java` - Bike inventory management
- `Rental.java` - Rental transaction processing
- `Message.java` - Messaging system
- `Ticket.java` - Support ticket management

#### **Controller Classes**
- `UserController.java` - User management logic
- `BikeController.java` - Bike operations
- `RentalController.java` - Rental processing
- `MessageController.java` - Message handling

#### **View Classes**
- `LoginView.java` - Authentication interface
- `AdminDashboardView.java` - Admin control panel
- `CustomerDashboardView.java` - Customer interface

## 🔄 Business Logic Flow

### **Rental Process**
1. Customer browses available bikes
2. Selects bike and rental duration
3. System calculates cost (applies promo if any)
4. Checks wallet balance
5. Creates rental record (pending approval)
6. Admin approves/rejects rental
7. Rental becomes active upon approval
8. System updates bike availability

### **Wallet Management**
- Pre-paid wallet system
- Transaction history tracking
- Balance validation before rentals
- Admin can add funds to customer wallets

## 📊 Data Validation

### **Input Validation**
- Email format validation
- Phone number formatting
- Date/time validation
- Numeric range checks
- String length limits

### **Business Rules**
- Cannot rent unavailable bikes
- Wallet balance must cover rental cost
- Promo codes have validity periods
- Users cannot approve their own rentals

## 🚀 Performance Considerations

### **Database Optimization**
- Indexed primary keys
- Foreign key relationships
- Proper data types selection
- Efficient query design

### **Memory Management**
- Connection pooling implementation
- Proper resource cleanup
- Efficient data loading strategies

## 🔧 API Endpoints (Internal)

### **User Management**
- `createUser()` - User registration
- `authenticateUser()` - Login validation
- `updateUserProfile()` - Profile updates

### **Bike Operations**
- `getAvailableBikes()` - Bike listing
- `updateBikeStatus()` - Availability management
- `calculateRentalCost()` - Pricing logic

### **Rental Processing**
- `createRental()` - New rental creation
- `approveRental()` - Admin approval
- `completeRental()` - Rental completion

## 🧪 Testing Strategy

### **Manual Test Cases**
- User authentication flow
- Bike rental process
- Wallet transactions
- Admin functionalities
- Error handling scenarios

### **Data Integrity Tests**
- Foreign key constraint validation
- Transaction rollback scenarios
- Concurrent access handling
- Data consistency checks

## 📈 Scalability Considerations

### **Current Capacity**
- Supports multiple concurrent users
- Efficient database query design
- Modular architecture for easy expansion

### **Future Enhancements**
- Database connection pooling
- Caching layer implementation
- Microservices architecture readiness