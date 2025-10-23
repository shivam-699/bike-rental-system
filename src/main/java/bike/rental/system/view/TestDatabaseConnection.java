package bike.rental.system.view;

import bike.rental.system.controller.UserController;
import bike.rental.system.controller.BikeController;
import bike.rental.system.controller.RentalController;
import bike.rental.system.controller.PaymentController;
import bike.rental.system.model.User;
import bike.rental.system.util.DatabaseConnection;
import bike.rental.system.model.Bike;
import bike.rental.system.model.Rental;
import bike.rental.system.model.Payment;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        UserController userController = new UserController();
        BikeController bikeController = new BikeController();
        RentalController rentalController = new RentalController();
        PaymentController paymentController = new PaymentController();
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection successfully!");

                // User CRUD
                String uniqueEmail = "john" + System.currentTimeMillis() + "@example.com"; // Unique email
                User user = new User(0, "John Doe", uniqueEmail, "password123", "1234567890", "customer", null);
                userController.createUser(user);
                System.out.println("User created with ID: " + user.getUserId());
            
                // Read
                User readUser = userController.readUser(user.getUserId());
                if (readUser != null) {
                    System.out.println("Read User: " + readUser.getName() + ", Email: " + readUser.getEmail());
                    readUser.setName("John Updated");
                    userController.updateUser(readUser);
                    System.out.println("User updated: " + readUser.getName());
                    userController.deleteUser(user.getUserId());
                    System.out.println("User with ID " + user.getUserId() + " deleted");
                } else {
                    System.out.println("Read User failed: User not found");
                }

                // Bike CRUD
                Bike bike = new Bike();
                bike.setBikeId(0);
                bike.setBrand("Trek");
                bike.setModel("FX 1");
                bike.setPricePerHour(5.0);
                bike.setStatus("available");
                bike.setConditionDescription("Good condition");
                bike.setCreatedAt(null); // Set to null or a valid Timestamp if needed
                bikeController.createBike(bike);
                System.out.println("Bike created with ID: " + bike.getBikeId());
            
                Bike readBike = bikeController.readBike(bike.getBikeId());
                if (readBike != null) {
                    System.out.println("Read Bike: " + readBike.getBrand() + " " + readBike.getModel());
                    readBike.setPricePerHour(6.0);
                    bikeController.updateBike(readBike);
                    System.out.println("Bike updated: Price per hour = " + readBike.getPricePerHour());
                    bikeController.deleteBike(bike.getBikeId());
                    System.out.println("Bike with ID " + bike.getBikeId() + " deleted");
                }

                // Rental CRUD
                User tempUser = new User(0, "Temp User", "temp" + System.currentTimeMillis() + "@example.com", "temp123", "0987654321", "customer", null);
                userController.createUser(tempUser);
                System.out.println("Temp User created with ID: " + tempUser.getUserId());

                Bike tempBike = new Bike();
                tempBike.setBikeId(0);
                tempBike.setBrand("Temp Bike");
                tempBike.setModel("Model X");
                tempBike.setPricePerHour(4.0);
                tempBike.setStatus("available");
                tempBike.setConditionDescription("Good");
                tempBike.setCreatedAt(null);
                bikeController.createBike(tempBike);
                System.out.println("Temp Bike created with ID: " + tempBike.getBikeId());
            
                Timestamp startTime = new Timestamp(new Date().getTime());
                Timestamp endTime = new Timestamp(new Date().getTime() + 3600000); // 1 hour later
                Rental rental = new Rental(0, tempUser.getUserId(), tempBike.getBikeId(), startTime, endTime, 6.0);
                rentalController.createRental(rental);
                System.out.println("Rental created with ID: " + rental.getRentalId());

                Rental readRental = rentalController.readRental(rental.getRentalId());
                if (readRental != null) {
                    System.out.println("Read Rental: User ID " + readRental.getUserId() + ", Bike ID " + readRental.getBikeId());
                    readRental.setTotalCost(7.0);
                    rentalController.updateRental(readRental);
                    System.out.println("Rental updated: Total cost = " + readRental.getTotalCost());
                }

                // Payment CRUD
                Payment payment = new Payment(0, rental.getRentalId(), 7.0, new Timestamp(new Date().getTime()), "completed");
                paymentController.createPayment(payment);
                System.out.println("Payment created with ID: " + payment.getPaymentId());

                Payment readPayment = paymentController.readPayment(payment.getPaymentId());
                if (readPayment != null) {
                    System.out.println("Read Payment: Rental ID " + readPayment.getRentalId() + ", Amount " + readPayment.getAmount());
                    readPayment.setStatus("processed");
                    paymentController.updatePayment(readPayment);
                    System.out.println("Payment updated: Status = " + readPayment.getStatus());
                    paymentController.deletePayment(payment.getPaymentId());
                    System.out.println("Payment with ID " + payment.getPaymentId() + " deleted");
                }

                // Delete rental after payment
                rentalController.deleteRental(rental.getRentalId());
                System.out.println("Rental with ID " + rental.getRentalId() + " deleted");

                // Clean up temp user and bike
                userController.deleteUser(tempUser.getUserId());
                System.out.println("Temp User with ID " + tempUser.getUserId() + " deleted");

                bikeController.deleteBike(tempBike.getBikeId());
                System.out.println("Temp Bike with ID " + tempBike.getBikeId() + " deleted");

                // DatabaseConnection.closeConnection();
            } else {
                System.out.println("Connection is null or closed!");
            }
        } catch (SQLException e) {
            System.err.println("Operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}