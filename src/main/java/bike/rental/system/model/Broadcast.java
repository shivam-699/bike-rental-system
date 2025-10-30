package bike.rental.system.model;

import java.sql.Timestamp;

public class Broadcast {
    private int broadcastId;
    private String text;
    private Timestamp createdAt;
    
    public Broadcast() {}
    
    public Broadcast(int broadcastId, String text, Timestamp createdAt) {
        this.broadcastId = broadcastId;
        this.text = text;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getBroadcastId() {
        return broadcastId;
    }
    
    public void setBroadcastId(int broadcastId) {
        this.broadcastId = broadcastId;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Broadcast{" +
                "broadcastId=" + broadcastId +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}