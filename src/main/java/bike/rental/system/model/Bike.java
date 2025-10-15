package bike.rental.system.model;

import java.sql.Timestamp;

public class Bike {
    private int bikeId;
    private String brand;
    private String model;
    private double pricePerHour;
    private String status;
    private String conditionDescription;
    private Timestamp createdAt;

    public Bike() {}
    public Bike(int bikeId, String brand, String model, double pricePerHour, String status, String conditionDescription, Timestamp createdAt) {
        this.bikeId = bikeId;
        this.brand = brand;
        this.model = model;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.conditionDescription = conditionDescription;
        this.createdAt = createdAt;
    }

    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getConditionDescription() { return conditionDescription; }
    public void setConditionDescription(String conditionDescription) { this.conditionDescription = conditionDescription; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}