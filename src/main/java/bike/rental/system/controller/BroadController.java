package bike.rental.system.controller;

import bike.rental.system.model.Broadcast;
import bike.rental.system.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BroadController {
    
    /**
     * Create a new broadcast message
     */
    public boolean createBroadcast(String message) {
        String sql = "INSERT INTO broadcasts (text, created_at) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, message);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all broadcasts (for admin view)
     */
    public List<Broadcast> getAllBroadcasts() {
        List<Broadcast> broadcasts = new ArrayList<>();
        String sql = "SELECT * FROM broadcasts ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Broadcast broadcast = new Broadcast(
                    rs.getInt("broadcast_id"),
                    rs.getString("text"),
                    rs.getTimestamp("created_at")
                );
                broadcasts.add(broadcast);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return broadcasts;
    }
    
    /**
     * Get the most recent broadcast (for customer view)
     */
    public Broadcast getLatestBroadcast() {
        String sql = "SELECT * FROM broadcasts ORDER BY created_at DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return new Broadcast(
                    rs.getInt("broadcast_id"),
                    rs.getString("text"),
                    rs.getTimestamp("created_at")
                );
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get broadcasts from the last X days
     */
    public List<Broadcast> getRecentBroadcasts(int days) {
        List<Broadcast> broadcasts = new ArrayList<>();
        String sql = "SELECT * FROM broadcasts WHERE created_at >= ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Calculate date X days ago
            long timeMillis = System.currentTimeMillis() - (days * 24L * 60 * 60 * 1000);
            Timestamp sinceDate = new Timestamp(timeMillis);
            
            stmt.setTimestamp(1, sinceDate);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Broadcast broadcast = new Broadcast(
                    rs.getInt("broadcast_id"),
                    rs.getString("text"),
                    rs.getTimestamp("created_at")
                );
                broadcasts.add(broadcast);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return broadcasts;
    }
    
    /**
     * Delete a broadcast by ID
     */
    public boolean deleteBroadcast(int broadcastId) {
        String sql = "DELETE FROM broadcasts WHERE broadcast_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, broadcastId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get broadcast count (for admin dashboard stats)
     */
    public int getBroadcastCount() {
        String sql = "SELECT COUNT(*) FROM broadcasts";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Check if there are any new broadcasts since the last check
     */
    public boolean hasNewBroadcasts(Timestamp lastCheck) {
        String sql = "SELECT COUNT(*) FROM broadcasts WHERE created_at > ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, lastCheck);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
}