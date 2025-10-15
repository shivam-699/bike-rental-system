package bike.rental.system.controller;

import bike.rental.system.model.DatabaseConnection;
import bike.rental.system.model.Bike;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BikeController {
    public void createBike(Bike bike) throws SQLException {
        String sql = "INSERT INTO bikes (brand, model, price_per_hour, status, condition_description) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"bike_id"})) {
            pstmt.setString(1, bike.getBrand());
            pstmt.setString(2, bike.getModel());
            pstmt.setDouble(3, bike.getPricePerHour());
            pstmt.setString(4, bike.getStatus());
            pstmt.setString(5, bike.getConditionDescription());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                bike.setBikeId(rs.getInt(1));
            }
        }
    }

    public Bike readBike(int bikeId) throws SQLException {
        String sql = "SELECT * FROM bikes WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bikeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Bike bike = new Bike();
                bike.setBikeId(rs.getInt("bike_id"));
                bike.setBrand(rs.getString("brand"));
                bike.setModel(rs.getString("model"));
                bike.setPricePerHour(rs.getDouble("price_per_hour"));
                bike.setStatus(rs.getString("status"));
                bike.setConditionDescription(rs.getString("condition_description"));
                bike.setCreatedAt(rs.getTimestamp("created_at"));
                return bike;
            }
            return null;
        }
    }

    public void updateBike(Bike bike) throws SQLException {
        String sql = "UPDATE bikes SET brand = ?, model = ?, price_per_hour = ?, status = ?, condition_description = ? WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bike.getBrand());
            pstmt.setString(2, bike.getModel());
            pstmt.setDouble(3, bike.getPricePerHour());
            pstmt.setString(4, bike.getStatus());
            pstmt.setString(5, bike.getConditionDescription());
            pstmt.setInt(6, bike.getBikeId());
            pstmt.executeUpdate();
        }
    }

    public void deleteBike(int bikeId) throws SQLException {
        String sql = "DELETE FROM bikes WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bikeId);
            pstmt.executeUpdate();
        }
    }
}
