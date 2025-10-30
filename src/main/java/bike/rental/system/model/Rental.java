package bike.rental.system.model;

import java.sql.Timestamp;

/**
 * Represents a bike rental transaction in the Bike Rental System.
 * 
 * <p>This class stores rental information including user and bike details,
 * rental period, costs, and transaction status. It serves as the central
 * entity for managing bike rental operations and tracking rental history.
 * 
 * @author Shivam
 * @version 1.0
 * @since 2024
 */
public class Rental {
    /** Unique identifier for the rental transaction */
    private int rentalId;
    
    /** ID of the user who rented the bike */
    private int userId;
    
    /** ID of the bike being rented */
    private int bikeId;
    
    /** Start date and time of the rental period */
    private Timestamp startTime;
    
    /** End date and time of the rental period */
    private Timestamp endTime;
    
    /** Total cost of the rental in local currency */
    private double totalCost;
    
    /** Current status of the rental: 'pending', 'active', 'completed', or 'rejected' */
    private String status;
    
    /** Brand of the rented bike (for display purposes) */
    private String bikeBrand;
    
    /** Model of the rented bike (for display purposes) */
    private String bikeModel;

    /**
     * Default constructor.
     */
    public Rental() {}

    /**
     * Parameterized constructor to create a rental with basic attributes.
     *
     * @param rentalId the unique rental identifier
     * @param userId the ID of the user renting the bike
     * @param bikeId the ID of the bike being rented
     * @param startTime the start date and time of rental
     * @param endTime the end date and time of rental
     * @param totalCost the total cost of the rental
     */
    public Rental(int rentalId, int userId, int bikeId, Timestamp startTime, Timestamp endTime, double totalCost) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
    }

    // Getters and Setters with comprehensive Javadoc

    /**
     * Gets the unique rental identifier.
     *
     * @return the rental ID
     */
    public int getRentalId() { return rentalId; }

    /**
     * Sets the unique rental identifier.
     *
     * @param rentalId the rental ID to set
     */
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    /**
     * Gets the ID of the user who rented the bike.
     *
     * @return the user ID
     */
    public int getUserId() { return userId; }

    /**
     * Sets the ID of the user who rented the bike.
     *
     * @param userId the user ID to set
     */
    public void setUserId(int userId) { this.userId = userId; }

    /**
     * Gets the ID of the bike being rented.
     *
     * @return the bike ID
     */
    public int getBikeId() { return bikeId; }

    /**
     * Sets the ID of the bike being rented.
     *
     * @param bikeId the bike ID to set
     */
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }

    /**
     * Gets the start date and time of the rental period.
     *
     * @return the rental start timestamp
     */
    public Timestamp getStartTime() { return startTime; }

    /**
     * Sets the start date and time of the rental period.
     *
     * @param startTime the rental start timestamp to set
     */
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    /**
     * Gets the end date and time of the rental period.
     *
     * @return the rental end timestamp
     */
    public Timestamp getEndTime() { return endTime; }

    /**
     * Sets the end date and time of the rental period.
     *
     * @param endTime the rental end timestamp to set
     */
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    /**
     * Gets the total cost of the rental.
     *
     * @return the total cost in local currency
     */
    public double getTotalCost() { return totalCost; }

    /**
     * Sets the total cost of the rental.
     *
     * @param totalCost the total cost to set
     */
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    /**
     * Gets the current status of the rental.
     *
     * @return the rental status: 'pending', 'active', 'completed', or 'rejected'
     */
    public String getStatus() { return status; }

    /**
     * Sets the current status of the rental.
     *
     * @param status the rental status to set: 'pending', 'active', 'completed', or 'rejected'
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Gets the brand of the rented bike.
     *
     * @return the bike brand for display purposes
     */
    public String getBikeBrand() { return bikeBrand; }

    /**
     * Sets the brand of the rented bike.
     *
     * @param bikeBrand the bike brand to set for display purposes
     */
    public void setBikeBrand(String bikeBrand) { this.bikeBrand = bikeBrand; }

    /**
     * Gets the model of the rented bike.
     *
     * @return the bike model for display purposes
     */
    public String getBikeModel() { return bikeModel; }

    /**
     * Sets the model of the rented bike.
     *
     * @param bikeModel the bike model to set for display purposes
     */
    public void setBikeModel(String bikeModel) { this.bikeModel = bikeModel; }
}