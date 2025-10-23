package bike.rental.system.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import bike.rental.system.util.DatabaseConnection;

public class CustomerDashboardModel {
    public List<Bike> getAvailableBikes() throws SQLException {
        List<Bike> bikes = new ArrayList<>();
        String sql = "SELECT * FROM bikes WHERE status = 'available' ORDER BY brand, model";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Bike bike = new Bike();
                bike.setBikeId(rs.getInt("bike_id"));
                bike.setBrand(rs.getString("brand"));
                bike.setModel(rs.getString("model"));
                bike.setPricePerHour(rs.getDouble("price_per_hour"));
                bike.setStatus(rs.getString("status"));
                bikes.add(bike);
            }
        }
        return bikes;
    }

    public List<Rental> getRentalHistory(int userId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, b.brand, b.model FROM rentals r " +
                    "JOIN bikes b ON r.bike_id = b.bike_id " +
                    "WHERE r.user_id = ? ORDER BY r.start_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Rental rental = new Rental();
                rental.setRentalId(rs.getInt("rental_id"));
                rental.setUserId(rs.getInt("user_id"));
                rental.setBikeId(rs.getInt("bike_id"));
                rental.setStartTime(rs.getTimestamp("start_time"));
                rental.setEndTime(rs.getTimestamp("end_time"));
                rental.setTotalCost(rs.getDouble("total_cost"));
                rental.setBikeBrand(rs.getString("brand"));
                rental.setBikeModel(rs.getString("model"));
                rentals.add(rental);
            }
        }
        return rentals;
    }
}