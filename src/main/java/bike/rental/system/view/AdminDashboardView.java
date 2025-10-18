package bike.rental.system.view;

import bike.rental.system.model.AdminDashboardModel;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AdminDashboardView extends JFrame {
    private JLabel totalRentalsLabel;
    private JLabel totalRevenueLabel;
    private JLabel availableBikesLabel;

    public AdminDashboardView() throws SQLException {
        AdminDashboardModel model = new AdminDashboardModel();
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
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

        add(panel);
        setVisible(true);
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