package bike.rental.system.controller;

import bike.rental.system.model.Payment;
import bike.rental.system.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentController {
    public void createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (rental_id, amount, payment_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, new String[]{"payment_id"})) {
            pstmt.setInt(1, payment.getRentalId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setTimestamp(3, payment.getPaymentDate());
            pstmt.setString(4, payment.getStatus());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt(1));
            }
        }
    }

    public Payment readPayment(int paymentId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setRentalId(rs.getInt("rental_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                payment.setStatus(rs.getString("status"));
                return payment;
            }
            return null;
        }
    }

    public void updatePayment(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET rental_id = ?, amount = ?, payment_date = ?, status = ? WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payment.getRentalId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setTimestamp(3, payment.getPaymentDate());
            pstmt.setString(4, payment.getStatus());
            pstmt.setInt(5, payment.getPaymentId());
            pstmt.executeUpdate();
        }
    }

    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, paymentId);
            pstmt.executeUpdate();
        }
    }
}