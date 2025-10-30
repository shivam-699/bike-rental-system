package bike.rental.system.model;

import java.sql.Timestamp;

/**
 * Represents a message in the Bike Rental System communication system.
 * 
 * <p>This class stores message information for communication between users
 * and administrators. It supports direct messaging with read status tracking
 * and timestamps for conversation management.
 * 
 * @author Shivam
 * @version 1.0
 * @since 2024
 */
public class Message {
    /** Unique identifier for the message */
    private int messageId;
    
    /** ID of the user who sent the message */
    private int fromUserId;
    
    /** ID of the user who received the message */
    private int toUserId;
    
    /** Content of the message */
    private String text;
    
    /** Timestamp when the message was sent */
    private Timestamp sentAt;
    
    /** Indicates whether the message has been read by the recipient */
    private boolean isRead;

    /**
     * Default constructor.
     */
    public Message() {}

    /**
     * Parameterized constructor to create a message with all attributes.
     *
     * @param messageId the unique message identifier
     * @param fromUserId the ID of the user sending the message
     * @param toUserId the ID of the user receiving the message
     * @param text the content of the message
     * @param sentAt the timestamp when the message was sent
     * @param isRead whether the message has been read by the recipient
     */
    public Message(int messageId, int fromUserId, int toUserId, String text, Timestamp sentAt, boolean isRead) {
        this.messageId = messageId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.text = text;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    // Getters and Setters with comprehensive Javadoc

    /**
     * Gets the unique message identifier.
     *
     * @return the message ID
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Sets the unique message identifier.
     *
     * @param messageId the message ID to set
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * Gets the ID of the user who sent the message.
     *
     * @return the sender's user ID
     */
    public int getFromUserId() {
        return fromUserId;
    }

    /**
     * Sets the ID of the user who sent the message.
     *
     * @param fromUserId the sender's user ID to set
     */
    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    /**
     * Gets the ID of the user who received the message.
     *
     * @return the recipient's user ID
     */
    public int getToUserId() {
        return toUserId;
    }

    /**
     * Sets the ID of the user who received the message.
     *
     * @param toUserId the recipient's user ID to set
     */
    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    /**
     * Gets the content of the message.
     *
     * @return the message text content
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the content of the message.
     *
     * @param text the message text content to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the timestamp when the message was sent.
     *
     * @return the message sent timestamp
     */
    public Timestamp getSentAt() {
        return sentAt;
    }

    /**
     * Sets the timestamp when the message was sent.
     *
     * @param sentAt the message sent timestamp to set
     */
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    /**
     * Checks whether the message has been read by the recipient.
     *
     * @return true if the message has been read, false otherwise
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * Sets the read status of the message.
     *
     * @param isRead the read status to set (true if read, false if unread)
     */
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * Returns a string representation of the message.
     *
     * @return string containing message details for debugging and display
     */
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