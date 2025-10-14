package bike.rental.system;

import bike.rental.system.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            if(conn != null && !conn.isClosed()){
                System.out.println("Connection successfully!");
            }
            else{
                System.out.println("cConnection is null or closed!");
            }
            DatabaseConnection.closeConnection();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}