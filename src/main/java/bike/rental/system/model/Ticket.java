package bike.rental.system.model;

import java.sql.Timestamp;

/**
 * Represents a support ticket in the Bike Rental System.
 * 
 * <p>This class stores support ticket information for customer service
 * and issue tracking. Tickets allow users to report problems, ask questions,
 * or request assistance, with administrators providing responses through
 * the ticket reply system.
 * 
 * @author Shivam
 * @version 1.0
 * @since 2024
 */
public class Ticket {
    /** Unique identifier for the support ticket */
    private int ticketId;
    
    /** ID of the user who created the ticket */
    private int userId;
    
    /** Brief summary of the issue or question */
    private String subject;
    
    /** Detailed description of the issue or request */
    private String description;
    
    /** Current status of the ticket: 'open', 'in_progress', or 'closed' */
    private String status;
    
    /** Timestamp when the ticket was created */
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public Ticket() {}

    /**
     * Parameterized constructor to create a ticket with all attributes.
     *
     * @param ticketId the unique ticket identifier
     * @param userId the ID of the user creating the ticket
     * @param subject the brief summary of the issue
     * @param description the detailed description of the issue
     * @param status the current status of the ticket
     * @param createdAt the timestamp when the ticket was created
     */
    public Ticket(int ticketId, int userId, String subject, String description, String status, Timestamp createdAt) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters with comprehensive Javadoc

    /**
     * Gets the unique ticket identifier.
     *
     * @return the ticket ID
     */
    public int getTicketId() { return ticketId; }

    /**
     * Sets the unique ticket identifier.
     *
     * @param ticketId the ticket ID to set
     */
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }

    /**
     * Gets the ID of the user who created the ticket.
     *
     * @return the user ID of the ticket creator
     */
    public int getUserId() { return userId; }

    /**
     * Sets the ID of the user who created the ticket.
     *
     * @param userId the user ID of the ticket creator to set
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Gets the brief summary of the issue or question.
     *
     * @return the ticket subject
     */
    public String getSubject() { return subject; }

    /**
     * Sets the brief summary of the issue or question.
     *
     * @param subject the ticket subject to set
     */
    public void setSubject(String subject) { this.subject = subject; }

    /**
     * Gets the detailed description of the issue or request.
     *
     * @return the ticket description
     */
    public String getDescription() { return description; }

    /**
     * Sets the detailed description of the issue or request.
     *
     * @param description the ticket description to set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Gets the current status of the ticket.
     *
     * @return the ticket status: 'open', 'in_progress', or 'closed'
     */
    public String getStatus() { return status; }

    /**
     * Sets the current status of the ticket.
     *
     * @param status the ticket status to set: 'open', 'in_progress', or 'closed'
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Gets the timestamp when the ticket was created.
     *
     * @return the ticket creation timestamp
     */
    public Timestamp getCreatedAt() { return createdAt; }

    /**
     * Sets the timestamp when the ticket was created.
     *
     * @param createdAt the ticket creation timestamp to set
     */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}