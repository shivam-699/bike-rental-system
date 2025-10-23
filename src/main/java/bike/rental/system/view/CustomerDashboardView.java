package bike.rental.system.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import bike.rental.system.controller.BikeController;
import bike.rental.system.controller.RentalController;
import bike.rental.system.model.Bike;
import bike.rental.system.model.Rental;

public class CustomerDashboardView extends JFrame {
    private JTextField searchField;
    private DefaultListModel<String> bikeListModel;
    private JList<String> bikeList;
    private int userId; // Removed default value, set via constructor
    private DefaultListModel<String> rentalListModel;
    private JList<String> rentalList;

    public CustomerDashboardView(int userId) {
        this.userId = userId; // Set userId from parameter
        initUI();
    }

    private void initUI() {
        setTitle("Customer Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        System.out.println("Initializing Customer Dashboard for userId: " + userId); // Debug print

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchField.setToolTipText("Search bikes by brand or model");
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> updateBikeList());
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Bike List
        bikeListModel = new DefaultListModel<>();
        System.out.println("Updating bike list..."); // Debug print
        updateBikeList();
        bikeList = new JList<>(bikeListModel);
        add(new JScrollPane(bikeList), BorderLayout.CENTER);

        // Rental History Panel
        JPanel rentalPanel = new JPanel(new BorderLayout());
        rentalListModel = new DefaultListModel<>();
        System.out.println("Updating rental list..."); // Debug print
        updateRentalList();
        rentalList = new JList<>(rentalListModel);
        rentalPanel.add(new JLabel("Rental History:"), BorderLayout.NORTH);
        rentalPanel.add(new JScrollPane(rentalList), BorderLayout.CENTER);
        add(rentalPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void updateBikeList() {
        bikeListModel.clear();
        String searchTerm = searchField.getText().toLowerCase();
        BikeController bikeController = new BikeController();
        List<Bike> bikes = bikeController.getAvailableBikes();
        for (Bike bike : bikes) {
            String bikeInfo = bike.getBikeId() + " | " + bike.getBrand() + " " + bike.getModel() + 
                            " | ₹" + bike.getPricePerHour() + "/hour | Status: " + bike.getStatus();
            if (searchTerm.isEmpty() || bike.getBrand().toLowerCase().contains(searchTerm) || 
                bike.getModel().toLowerCase().contains(searchTerm)) {
                bikeListModel.addElement(bikeInfo);
            }
        }
    }

    private void updateRentalList() {
        rentalListModel.clear();
        RentalController rentalController = new RentalController();
        List<Rental> rentals = rentalController.getRentalsByUserId(userId);
        for (Rental rental : rentals) {
            String rentalInfo = "ID: " + rental.getRentalId() + " | Bike: " + rental.getBikeId() + 
                              " | Brand: " + rental.getBikeBrand() + " | Model: " + rental.getBikeModel() + 
                              " | Start: " + rental.getStartTime() + " | End: " + rental.getEndTime() + 
                              " | Cost: ₹" + rental.getTotalCost() + " | Status: " + rental.getStatus();
            rentalListModel.addElement(rentalInfo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerDashboardView view = new CustomerDashboardView(24); // Example userId for testing
            view.setVisible(true);
        });
    }
}