package bike.rental.system;

import bike.rental.system.controller.UserController;
import bike.rental.system.model.User;
import bike.rental.system.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;


public class TestDatabaseConnection {
    public static void main(String[] args) {
        UserController userController = new UserController();
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successfully!");

                // Create with a unique email
                String uniqueEmail = "john" + System.currentTimeMillis() + "@example.com"; // Unique email
                User user = new User(0, "John Doe", uniqueEmail, "password123", "1234567890", "customer", null);
                userController.createUser(user);
                System.out.println("User created with ID: " + user.getUserId());
            
                // Read
                User readUser = userController.readUser(user.getUserId());
                if (readUser != null) {
                    System.out.println("Read User: " + readUser.getName() + ", Email: " + readUser.getEmail());
                } else {
                    System.out.println("Read User failed: User not found");
                }

                // Update
                if (readUser != null) {
                    readUser.setName("John Updated");
                    readUser.setEmail("john.updated@example.com");
                    userController.updateUser(readUser);
                    System.out.println("User updated: " + readUser.getName());
                }

                // Delete
                if (readUser != null) {
                    userController.deleteUser(user.getUserId());
                    System.out.println("User with ID " + user.getUserId() + " deleted");
                }

                DatabaseConnection.closeConnection();
            } else {
                System.out.println("Connection is null or closed!");
            }
        } catch (SQLException e) {
            System.err.println("Operation failed: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for detailed error
        }
    }
}