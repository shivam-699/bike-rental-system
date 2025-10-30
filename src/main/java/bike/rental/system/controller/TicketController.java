package bike.rental.system.controller;

import bike.rental.system.model.Ticket;
import bike.rental.system.model.TicketReply;
import bike.rental.system.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketController {
    
    public List<Ticket> getTicketsForUser(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSubject(rs.getString("subject"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setCreatedAt(rs.getTimestamp("created_at"));
                tickets.add(ticket);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tickets;
    }
    
    public void createTicket(int userId, String subject, String description) {
        String sql = "INSERT INTO tickets (user_id, subject, description, status, created_at) VALUES (?, ?, ?, 'open', ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, subject);
            stmt.setString(3, description);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Ticket> getOpenTicketsForAdmin() {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE status = 'open' ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSubject(rs.getString("subject"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setCreatedAt(rs.getTimestamp("created_at"));
                tickets.add(ticket);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tickets;
    }
    
    public void closeTicket(int ticketId) {
        String sql = "UPDATE tickets SET status = 'closed' WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<TicketReply> getReplies(int ticketId) {
        List<TicketReply> replies = new ArrayList<>();
        String sql = "SELECT * FROM ticket_replies WHERE ticket_id = ? ORDER BY created_at ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                TicketReply reply = new TicketReply();
                reply.setReplyId(rs.getInt("reply_id"));
                reply.setTicketId(rs.getInt("ticket_id"));
                reply.setUserId(rs.getInt("user_id"));
                reply.setText(rs.getString("text"));
                reply.setCreatedAt(rs.getTimestamp("created_at"));
                replies.add(reply);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return replies;
    }
    
    public void addReply(int ticketId, int userId, String text) {
        String sql = "INSERT INTO ticket_replies (ticket_id, user_id, text, created_at) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            stmt.setInt(2, userId);
            stmt.setString(3, text);
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}