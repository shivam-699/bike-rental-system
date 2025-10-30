package bike.rental.system.controller;

import bike.rental.system.model.PromoCode;
import bike.rental.system.util.DatabaseConnection;

import java.sql.*;
// import java.util.ArrayList;
// import java.util.List;

public class PromoController {

    public void createPromo(String code, double discount, Date from, Date to, Integer uses) {
        String sql = "INSERT INTO promo_codes (code, discount_percent, valid_from, valid_to, uses_left) VALUES (?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setDouble(2, discount);
            ps.setDate(3, from);
            ps.setDate(4, to);
            if (uses == null) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, uses);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public PromoCode getValidPromo(String code) {
        String sql = "SELECT * FROM promo_codes WHERE code = ? AND (valid_from IS NULL OR valid_from <= CURDATE()) " +
                     "AND (valid_to IS NULL OR valid_to >= CURDATE()) AND (uses_left IS NULL OR uses_left > 0)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PromoCode p = new PromoCode();
                p.setCode(rs.getString("code"));
                p.setDiscountPercent(rs.getDouble("discount_percent"));
                p.setValidFrom(rs.getDate("valid_from"));
                p.setValidTo(rs.getDate("valid_to"));
                p.setUsesLeft(rs.getObject("uses_left") == null ? null : rs.getInt("uses_left"));
                return p;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void decrementUses(String code) {
        String sql = "UPDATE promo_codes SET uses_left = uses_left - 1 WHERE code = ? AND uses_left IS NOT NULL";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}