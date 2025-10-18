package bike.rental.system.view;

import bike.rental.system.controller.UserController;
import bike.rental.system.model.User;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserController userController;
    
    public LoginView() {
        userController = new UserController();
        setTitle("Bike Rental System - Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panels
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email label and field
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        // Add panel to frame
        add(panel);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim(); // Trim to avoid whitespace
                String password = new String(passwordField.getPassword()).trim(); // Trim password input
                try {
                    User user = userController.readUserByEmail(email);
                    System.out.println("Checking login for: " + email + ", input password: " + password + 
                                        ", retrieved hash: " + (user != null ? user.getPasswordHash() : "null"));
                    if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
                        JOptionPane.showMessageDialog(LoginView.this, "Login successful for: " + email);
                        if ("admin".equals(user.getRole())) {
                            JOptionPane.showMessageDialog(LoginView.this, "Redirecting to Admin Dashboard");
                            new AdminDashboardView();
                        } else {
                            JOptionPane.showMessageDialog(LoginView.this, "Redirecting to Customer Dashboard");
                        }
                        dispose(); // Close login window
                    } else {
                        JOptionPane.showMessageDialog(LoginView.this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(LoginView.this, "Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    passwordField.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}