package bike.rental.system.model;

import java.sql.Timestamp;

public class Rental {
    private int rentalId;
    private int userId;
    private int bikeId;
    private Timestamp startTime;
    private Timestamp endTime;
    private double totalCost;
    private String status; // From previous update
    private String bikeBrand; // New field
    private String bikeModel; // New field

    // Default constructor
    public Rental() {}

    // Constructor with parameters
    public Rental(int rentalId, int userId, int bikeId, Timestamp startTime, Timestamp endTime, double totalCost) {
        this.rentalId = rentalId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBikeBrand() { return bikeBrand; } // New getter
    public void setBikeBrand(String bikeBrand) { this.bikeBrand = bikeBrand; } // New setter
    public String getBikeModel() { return bikeModel; } // New getter
    public void setBikeModel(String bikeModel) { this.bikeModel = bikeModel; } // New setter
}