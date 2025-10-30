package bike.rental.system.model;

import java.sql.Timestamp;

/**
 * Represents a bike in the Bike Rental System.
 * 
 * <p>This class stores bike information including specifications,
 * rental pricing, availability status, and maintenance details.
 * Bikes can be in different states: available, unavailable, or maintenance.
 * 
 * @author Shivam
 * @version 1.0
 * @since 2024
 */
public class Bike {
    /** Unique identifier for the bike */
    private int bikeId;
    
    /** Manufacturer brand of the bike */
    private String brand;
    
    /** Model name of the bike */
    private String model;
    
    /** Rental price per hour in local currency */
    private double pricePerHour;
    
    /** Current availability status: 'available', 'unavailable', or 'maintenance' */
    private String status;
    
    /** Description of the bike's physical condition and maintenance history */
    private String conditionDescription;
    
    /** Timestamp when the bike was added to the system */
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public Bike() {}

    // Getters and Setters with comprehensive Javadoc

    /**
     * Gets the unique bike identifier.
     *
     * @return the bike ID
     */
    public int getBikeId() { return bikeId; }

    /**
     * Sets the unique bike identifier.
     *
     * @param bikeId the bike ID to set
     */
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }

    /**
     * Gets the manufacturer brand of the bike.
     *
     * @return the bike brand
     */
    public String getBrand() { return brand; }

    /**
     * Sets the manufacturer brand of the bike.
     *
     * @param brand the brand to set
     */
    public void setBrand(String brand) { this.brand = brand; }

    /**
     * Gets the model name of the bike.
     *
     * @return the bike model
     */
    public String getModel() { return model; }

    /**
     * Sets the model name of the bike.
     *
     * @param model the model to set
     */
    public void setModel(String model) { this.model = model; }

    /**
     * Gets the rental price per hour.
     *
     * @return the price per hour in local currency
     */
    public double getPricePerHour() { return pricePerHour; }

    /**
     * Sets the rental price per hour.
     *
     * @param pricePerHour the price per hour to set
     */
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }

    /**
     * Gets the current availability status of the bike.
     *
     * @return the status: 'available', 'unavailable', or 'maintenance'
     */
    public String getStatus() { return status; }

    /**
     * Sets the current availability status of the bike.
     *
     * @param status the status to set: 'available', 'unavailable', or 'maintenance'
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Gets the condition description of the bike.
     *
     * @return description of physical condition and maintenance history
     */
    public String getConditionDescription() { return conditionDescription; }

    /**
     * Sets the condition description of the bike.
     *
     * @param conditionDescription the condition description to set
     */
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }

    /**
     * Gets the timestamp when the bike was added to the system.
     *
     * @return the creation timestamp
     */
    public Timestamp getCreatedAt() { return createdAt; }

    /**
     * Sets the timestamp when the bike was added to the system.
     *
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}