package bike.rental.system.view;

import bike.rental.system.model.AdminDashboardModel;
import bike.rental.system.model.DatabaseConnection; // Add this import
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection; // Add this import
import java.sql.PreparedStatement; // Add this import
import java.sql.SQLException;

public class AdminDashboardView extends JFrame {
    private JLabel totalRentalsLabel;
    private JLabel totalRevenueLabel;
    private JLabel availableBikesLabel;
    private JTextField brandField, modelField, priceField;
    private AdminDashboardModel model;

    public AdminDashboardView() throws SQLException {
        model = new AdminDashboardModel();
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Total Rentals:"));
        totalRentalsLabel = new JLabel(String.valueOf(model.getTotalRentals()));
        panel.add(totalRentalsLabel);

        panel.add(new JLabel("Total Revenue:"));
        totalRevenueLabel = new JLabel(String.format("%.2f", model.getTotalRevenue()));
        panel.add(totalRevenueLabel);

        panel.add(new JLabel("Available Bikes:"));
        availableBikesLabel = new JLabel(String.valueOf(model.getAvailableBikes()));
        panel.add(availableBikesLabel);

        panel.add(new JLabel("Brand:"));
        brandField = new JTextField(10);
        panel.add(brandField);

        panel.add(new JLabel("Model:"));
        modelField = new JTextField(10);
        panel.add(modelField);

        panel.add(new JLabel("Price per Hour:"));
        priceField = new JTextField(10);
        panel.add(priceField);

        JButton addBikeButton = new JButton("Add Bike");
        addBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String brand = brandField.getText().trim();
                    String model = modelField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    if (addBikeToDatabase(brand, model, price)) {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Bike added successfully!");
                        refreshDashboard();
                    } else {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Failed to add bike.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(addBikeButton);

        add(panel);
        setVisible(true);
    }

    private boolean addBikeToDatabase(String brand, String model, double price) throws SQLException {
        String sql = "INSERT INTO bikes (brand, model, price_per_hour, status) VALUES (?, ?, ?, 'available')";
        try (Connection conn = DatabaseConnection.getConnection(); // Explicit type
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Explicit type
            pstmt.setString(1, brand);
            pstmt.setString(2, model);
            pstmt.setDouble(3, price);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    private void refreshDashboard() throws SQLException {
        totalRentalsLabel.setText(String.valueOf(model.getTotalRentals()));
        totalRevenueLabel.setText(String.format("%.2f", model.getTotalRevenue()));
        availableBikesLabel.setText(String.valueOf(model.getAvailableBikes()));
        brandField.setText("");
        modelField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new AdminDashboardView();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error loading dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}