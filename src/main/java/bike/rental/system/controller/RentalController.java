package bike.rental.system.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import bike.rental.system.model.Rental;
import bike.rental.system.util.DatabaseConnection;

public class RentalController {
    public void createRental(Rental rental) throws SQLException {
    String sql = "INSERT INTO rentals (user_id, bike_id, start_time, end_time, total_cost, status) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, rental.getUserId());
        stmt.setInt(2, rental.getBikeId());
        stmt.setTimestamp(3, rental.getStartTime());
        stmt.setTimestamp(4, rental.getEndTime());
        stmt.setDouble(5, rental.getTotalCost());
        stmt.setString(6, rental.getStatus()); // Added
        stmt.executeUpdate();
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                rental.setRentalId(generatedKeys.getInt(1));
            }
        }
    }
}

public Rental readRental(int rentalId) throws SQLException {
    String sql = "SELECT * FROM rentals WHERE rental_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, rentalId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Rental rental = new Rental();
                rental.setRentalId(rs.getInt("rental_id"));
                rental.setUserId(rs.getInt("user_id"));
                rental.setBikeId(rs.getInt("bike_id"));
                rental.setStartTime(rs.getTimestamp("start_time"));
                rental.setEndTime(rs.getTimestamp("end_time"));
                rental.setTotalCost(rs.getDouble("total_cost"));
                rental.setStatus(rs.getString("status")); // Added
                return rental;
            }
        }
    }
    return null;
}

    public void updateRental(Rental rental) throws SQLException {
        String sql = "UPDATE rentals SET user_id = ?, bike_id = ?, start_time = ?, end_time = ?, total_cost = ? WHERE rental_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rental.getUserId());
            stmt.setInt(2, rental.getBikeId());
            stmt.setTimestamp(3, rental.getStartTime());
            stmt.setTimestamp(4, rental.getEndTime());
            stmt.setDouble(5, rental.getTotalCost());
            stmt.setInt(6, rental.getRentalId());
            stmt.executeUpdate();
        }
    }

    public void deleteRental(int rentalId) throws SQLException {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rentalId);
            stmt.executeUpdate();
        }
    }

    // Existing method from previous step
    public List<Rental> getRentalsByUserId(int userId) {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rental rental = new Rental();
                    rental.setRentalId(rs.getInt("rental_id"));
                    rental.setUserId(rs.getInt("user_id"));
                    rental.setBikeId(rs.getInt("bike_id"));
                    rental.setStartTime(rs.getTimestamp("start_time"));
                    rental.setEndTime(rs.getTimestamp("end_time"));
                    rental.setTotalCost(rs.getDouble("total_cost"));
                    rentals.add(rental);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentals;
    }


    public boolean createRental(int userId, int bikeId, int hours, double totalCost, String promoCode) {
        String sql = "INSERT INTO rentals (user_id, bike_id, start_time, end_time, total_cost, promo_code, status) VALUES (?, ?, ?, ?, ?, ?, 'pending')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            Timestamp endTime = new Timestamp(System.currentTimeMillis() + (hours * 60L * 60 * 1000));
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bikeId);
            stmt.setTimestamp(3, startTime);
            stmt.setTimestamp(4, endTime);
            stmt.setDouble(5, totalCost);
            stmt.setString(6, promoCode.isEmpty() ? null : promoCode);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}