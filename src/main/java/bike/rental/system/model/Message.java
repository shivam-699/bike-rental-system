package bike.rental.system.model;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private int fromUserId;
    private int toUserId;
    private String text;
    private Timestamp sentAt;
    private boolean isRead;
    
    // Default constructor
    public Message() {}
    
    // Parameterized constructor
    public Message(int messageId, int fromUserId, int toUserId, String text, Timestamp sentAt, boolean isRead) {
        this.messageId = messageId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.text = text;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }
    
    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }
    
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    
    public int getFromUserId() {
        return fromUserId;
    }
    
    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }
    
    public int getToUserId() {
        return toUserId;
    }
    
    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Timestamp getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", text='" + text + '\'' +
                ", sentAt=" + sentAt +
                ", isRead=" + isRead +
                '}';
    }
}