package bike.rental.system.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
// import java.sql.Timestamp;

import bike.rental.system.model.Bike;
import bike.rental.system.util.DatabaseConnection; // Verify this resolves

public class BikeController {
    public List<Bike> getAvailableBikes() {
        List<Bike> bikes = new ArrayList<>();
        String sql = "SELECT bike_id, brand, model, price_per_hour, status, condition_description, created_at " +
                     "FROM bikes WHERE status = 'available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Bike bike = new Bike();
                bike.setBikeId(rs.getInt("bike_id"));
                bike.setBrand(rs.getString("brand"));
                bike.setModel(rs.getString("model"));
                bike.setPricePerHour(rs.getDouble("price_per_hour"));
                bike.setStatus(rs.getString("status"));
                bike.setConditionDescription(rs.getString("condition_description"));
                bike.setCreatedAt(rs.getTimestamp("created_at"));
                bikes.add(bike);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching available bikes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bikes;
    }

    public void createBike(Bike bike) throws SQLException {
        String sql = "INSERT INTO bikes (brand, model, price_per_hour, status, condition_description, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bike.getBrand());
            stmt.setString(2, bike.getModel());
            stmt.setDouble(3, bike.getPricePerHour());
            stmt.setString(4, bike.getStatus());
            stmt.setString(5, bike.getConditionDescription());
            stmt.setTimestamp(6, bike.getCreatedAt());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bike.setBikeId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Bike readBike(int bikeId) throws SQLException {
        String sql = "SELECT * FROM bikes WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bikeId);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        }
        return null;
    }

    public void updateBike(Bike bike) throws SQLException {
        String sql = "UPDATE bikes SET brand = ?, model = ?, price_per_hour = ?, status = ?, condition_description = ?, created_at = ? WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bike.getBrand());
            stmt.setString(2, bike.getModel());
            stmt.setDouble(3, bike.getPricePerHour());
            stmt.setString(4, bike.getStatus());
            stmt.setString(5, bike.getConditionDescription());
            stmt.setTimestamp(6, bike.getCreatedAt());
            stmt.setInt(7, bike.getBikeId());
            stmt.executeUpdate();
        }
    }

    public void deleteBike(int bikeId) throws SQLException {
        String sql = "DELETE FROM bikes WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bikeId);
            stmt.executeUpdate();
        }
    }

    public boolean updateBikeStatus(int bikeId, String status) {
    String sql = "UPDATE bikes SET status = ? WHERE bike_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, status);
        stmt.setInt(2, bikeId);
        
        return stmt.executeUpdate() > 0;
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating bike status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
}