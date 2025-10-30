package bike.rental.system.model;

import java.sql.Timestamp;

public class TicketReply {
    private int replyId;
    private int ticketId;
    private int userId;
    private String text;
    private Timestamp createdAt;
    
    // Constructors
    public TicketReply() {}
    
    public TicketReply(int replyId, int ticketId, int userId, String text, Timestamp createdAt) {
        this.replyId = replyId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.text = text;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getReplyId() { return replyId; }
    public void setReplyId(int replyId) { this.replyId = replyId; }
    
    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}