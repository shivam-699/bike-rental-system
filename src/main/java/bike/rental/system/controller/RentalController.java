package bike.rental.system.controller;

import bike.rental.system.model.DatabaseConnection;
import bike.rental.system.model.Rental;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RentalController {
    public void createRental(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (user_id, bike_id, start_time, end_time, total_cost) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"rental_id"})) {
            pstmt.setInt(1, rental.getUserId());
            pstmt.setInt(2, rental.getBikeId());
            pstmt.setTimestamp(3, rental.getStartTime());
            pstmt.setTimestamp(4, rental.getEndTime());
            pstmt.setDouble(5, rental.getTotalCost());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                rental.setRentalId(rs.getInt(1));
            }
        }
    }

    public Rental readRental(int rentalId) throws SQLException {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rentalId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Rental rental = new Rental();
                rental.setRentalId(rs.getInt("rental_id"));
                rental.setUserId(rs.getInt("user_id"));
                rental.setBikeId(rs.getInt("bike_id"));
                rental.setStartTime(rs.getTimestamp("start_time"));
                rental.setEndTime(rs.getTimestamp("end_time"));
                rental.setTotalCost(rs.getDouble("total_cost"));
                return rental;
            }
            return null;
        }
    }

    public void updateRental(Rental rental) throws SQLException {
        String sql = "UPDATE rentals SET user_id = ?, bike_id = ?, start_time = ?, end_time = ?, total_cost = ? WHERE rental_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rental.getUserId());
            pstmt.setInt(2, rental.getBikeId());
            pstmt.setTimestamp(3, rental.getStartTime());
            pstmt.setTimestamp(4, rental.getEndTime());
            pstmt.setDouble(5, rental.getTotalCost());
            pstmt.setInt(6, rental.getRentalId());
            pstmt.executeUpdate();
        }
    }

    public void deleteRental(int rentalId) throws SQLException {
        String sql = "DELETE FROM rentals WHERE rental_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, rentalId);
            pstmt.executeUpdate();
        }
    }
}