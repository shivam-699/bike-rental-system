package bike.rental.system.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bike_rental_system";
    private static final String USER = "root";
    private static final String PASSWORD = "Shivam@12345"; // Replace with your MySQL root password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void closeConnection() throws SQLException {
        Connection conn = getConnection();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}