package bike.rental.system.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardModel {
    public int getTotalRentals() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM rentals";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(total_cost) AS revenue FROM rentals";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("revenue");
            }
            return 0.0;
        }
    }

    public int getAvailableBikes() throws SQLException {
        String sql = "SELECT COUNT(*) AS available FROM bikes WHERE status = 'available'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("available");
            }
            return 0;
        }
    }
}