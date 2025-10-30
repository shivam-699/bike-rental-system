package bike.rental.system.controller;

import bike.rental.system.model.Message;
import bike.rental.system.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageController {
    
    public List<Message> getMessagesForUser(int userId) {
        List<Message> messages = new ArrayList<>();
        // FIXED: Use correct column names (from_user, to_user instead of from_user_id, to_user_id)
        String sql = "SELECT * FROM messages WHERE to_user = ? OR from_user = ? ORDER BY sent_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("msg_id"));  // FIXED: msg_id instead of message_id
                message.setFromUserId(rs.getInt("from_user"));  // FIXED: from_user instead of from_user_id
                message.setToUserId(rs.getInt("to_user"));  // FIXED: to_user instead of to_user_id
                message.setText(rs.getString("text"));
                message.setSentAt(rs.getTimestamp("sent_at"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
    
    public void sendMessage(int fromUserId, int toUserId, String text) {
        // FIXED: Use correct column names
        String sql = "INSERT INTO messages (from_user, to_user, text, sent_at, is_read) VALUES (?, ?, ?, ?, false)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.setString(3, text);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Message> getUnreadForUser(int userId) {
        List<Message> messages = new ArrayList<>();
        // FIXED: Use correct column names
        String sql = "SELECT * FROM messages WHERE to_user = ? AND is_read = false ORDER BY sent_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message();
                message.setMessageId(rs.getInt("msg_id"));  // FIXED: msg_id instead of message_id
                message.setFromUserId(rs.getInt("from_user"));  // FIXED: from_user instead of from_user_id
                message.setToUserId(rs.getInt("to_user"));  // FIXED: to_user instead of to_user_id
                message.setText(rs.getString("text"));
                message.setSentAt(rs.getTimestamp("sent_at"));
                message.setRead(rs.getBoolean("is_read"));
                messages.add(message);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return messages;
    }
}