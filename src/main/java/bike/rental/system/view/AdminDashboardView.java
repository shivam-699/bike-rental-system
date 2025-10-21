package bike.rental.system.view;

import bike.rental.system.model.AdminDashboardModel;
import bike.rental.system.model.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardView extends JFrame {
    private JLabel totalRentalsLabel;
    private JLabel totalRevenueLabel;
    private JLabel availableBikesLabel;
    private JTextField brandField, modelField, priceField, bikeIdField, editBikeIdField, editBrandField, editModelField, editPriceField;
    private JTextArea bikeListArea;
    private AdminDashboardModel model;

    public AdminDashboardView() throws SQLException {
        model = new AdminDashboardModel();
        setTitle("Admin Dashboard");
        setSize(400, 700); // Increased size for bike list
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(13, 2, 10, 10));
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

        panel.add(new JLabel("Add Brand:"));
        brandField = new JTextField(10);
        panel.add(brandField);

        panel.add(new JLabel("Add Model:"));
        modelField = new JTextField(10);
        panel.add(modelField);

        panel.add(new JLabel("Add Price per Hour:"));
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

        panel.add(new JLabel("Bike ID to Delete:"));
        bikeIdField = new JTextField(10);
        panel.add(bikeIdField);

        JButton deleteBikeButton = new JButton("Delete Bike");
        deleteBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bikeId = Integer.parseInt(bikeIdField.getText().trim());
                    if (deleteBikeFromDatabase(bikeId)) {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Bike deleted successfully!");
                        refreshDashboard();
                    } else {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Failed to delete bike or bike not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Please enter a valid bike ID.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(deleteBikeButton);

        panel.add(new JLabel("Edit Bike ID:"));
        editBikeIdField = new JTextField(10);
        panel.add(editBikeIdField);

        panel.add(new JLabel("Edit Brand:"));
        editBrandField = new JTextField(10);
        panel.add(editBrandField);

        panel.add(new JLabel("Edit Model:"));
        editModelField = new JTextField(10);
        panel.add(editModelField);

        panel.add(new JLabel("Edit Price per Hour:"));
        editPriceField = new JTextField(10);
        panel.add(editPriceField);

        JButton editBikeButton = new JButton("Edit Bike");
        editBikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bikeId = Integer.parseInt(editBikeIdField.getText().trim());
                    String brand = editBrandField.getText().trim();
                    String model = editModelField.getText().trim();
                    double price = Double.parseDouble(editPriceField.getText().trim());
                    if (editBikeInDatabase(bikeId, brand, model, price)) {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Bike updated successfully!");
                        refreshDashboard();
                    } else {
                        JOptionPane.showMessageDialog(AdminDashboardView.this, "Failed to update bike or bike not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Please enter valid ID and price.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AdminDashboardView.this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(editBikeButton);

        panel.add(new JLabel("Bike List:"));
        bikeListArea = new JTextArea(5, 20);
        bikeListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bikeListArea);
        panel.add(scrollPane);

        JButton refreshListButton = new JButton("Refresh Bike List");
        refreshListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBikeList();
            }
        });
        panel.add(refreshListButton);

        // Initial refresh
        try {
            refreshDashboard();
            refreshBikeList();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading dashboard: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        add(panel);
        setVisible(true);
    }

    private boolean addBikeToDatabase(String brand, String model, double price) throws SQLException {
        String sql = "INSERT INTO bikes (brand, model, price_per_hour, status) VALUES (?, ?, ?, 'available')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, model);
            pstmt.setDouble(3, price);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    private boolean deleteBikeFromDatabase(int bikeId) throws SQLException {
        String sql = "DELETE FROM bikes WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bikeId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }
    }

    private boolean editBikeInDatabase(int bikeId, String brand, String model, double price) throws SQLException {
        String sql = "UPDATE bikes SET brand = ?, model = ?, price_per_hour = ? WHERE bike_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brand);
            pstmt.setString(2, model);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, bikeId);
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
        bikeIdField.setText("");
        editBikeIdField.setText("");
        editBrandField.setText("");
        editModelField.setText("");
        editPriceField.setText("");
    }

    private void refreshBikeList() {
        try {
            String sql = "SELECT bike_id, brand, model, price_per_hour, status FROM bikes";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                StringBuilder list = new StringBuilder();
                while (rs.next()) {
                    list.append("ID: ").append(rs.getInt("bike_id"))
                        .append(", Brand: ").append(rs.getString("brand"))
                        .append(", Model: ").append(rs.getString("model"))
                        .append(", Price: ").append(rs.getDouble("price_per_hour"))
                        .append(", Status: ").append(rs.getString("status"))
                        .append("\n");
                }
                bikeListArea.setText(list.toString());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error refreshing bike list: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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