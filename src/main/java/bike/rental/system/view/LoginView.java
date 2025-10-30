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
    private JCheckBox rememberMeCheck;
    private JProgressBar progressBar;
    private String currentLanguage = "en";

    public LoginView() {
        userController = new UserController();
        setTitle(getTranslatedTitle());
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create panels
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Email label and field
        JLabel emailLabel = new JLabel(getTranslatedText("Email:"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(emailField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel(getTranslatedText("Password:"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Remember Me Checkbox
        rememberMeCheck = new JCheckBox(getTranslatedText("Remember Me"));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(rememberMeCheck, gbc);

        // Forgot Password Link
        JButton forgotPasswordButton = new JButton(getTranslatedText("Forgot Password?"));
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.addActionListener(e -> JOptionPane.showMessageDialog(this, getTranslatedText("Contact support to reset your password.")));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(forgotPasswordButton, gbc);

        // Login button
        loginButton = new JButton(getTranslatedText("Login"));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(loginButton, gbc);

        // Register Button
        JButton registerButton = new JButton(getTranslatedText("Register"));
        registerButton.addActionListener(e -> JOptionPane.showMessageDialog(this, getTranslatedText("Registration feature coming soon!")));
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(registerButton, gbc);

        // Language Toggle
        JButton languageButton = new JButton(getTranslatedText("Toggle Language"));
        languageButton.addActionListener(e -> {
            currentLanguage = currentLanguage.equals("en") ? "hi" : "en";
            SwingUtilities.updateComponentTreeUI(this);
            setTitle(getTranslatedTitle());
            emailLabel.setText(getTranslatedText("Email:"));
            passwordLabel.setText(getTranslatedText("Password:"));
            rememberMeCheck.setText(getTranslatedText("Remember Me"));
            forgotPasswordButton.setText(getTranslatedText("Forgot Password?"));
            loginButton.setText(getTranslatedText("Login"));
            registerButton.setText(getTranslatedText("Register"));
            // languageButton.setText(getTranslatedText("Toggle Language"));
        });
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(languageButton, gbc);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panel.add(progressBar, gbc);

        // Add panel to frame
        add(panel);

        // Action listener for login button with loading animation
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                loginButton.setEnabled(false);

                new Thread(() -> {
                    try {
                        User user = userController.readUserByEmail(email);
                        System.out.println("Checking login for: " + email + ", input password: " + password + 
                                          ", retrieved hash: " + (user != null ? user.getPasswordHash() : "null"));
                        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
                            JOptionPane.showMessageDialog(LoginView.this, getTranslatedText("Login successful for: ") + email);
                            if ("admin".equals(user.getRole())) {
                                JOptionPane.showMessageDialog(LoginView.this, getTranslatedText("Redirecting to Admin Dashboard"));
                                new AdminDashboardView(user.getUserId()).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(LoginView.this, getTranslatedText("Redirecting to Customer Dashboard"));
                                new CustomerDashboardView(user.getUserId()).setVisible(true);
                            }
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(LoginView.this, getTranslatedText("Invalid email or password"), getTranslatedText("Login Failed"), JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(LoginView.this, getTranslatedText("Error during login: ") + ex.getMessage(), getTranslatedText("Error"), JOptionPane.ERROR_MESSAGE);
                    } finally {
                        SwingUtilities.invokeLater(() -> {
                            progressBar.setVisible(false);
                            progressBar.setIndeterminate(false);
                            loginButton.setEnabled(true);
                            passwordField.setText("");
                        });
                    }
                }).start();
            }
        });
    }

    private String getTranslatedText(String key) {
        if (currentLanguage.equals("hi")) {
            switch (key) {
                case "Email:": return "ईमेल:";
                case "Password:": return "पासवर्ड:";
                case "Remember Me": return "मुझे याद रखें";
                case "Forgot Password?": return "पासवर्ड भूल गए?";
                case "Login": return "लॉगिन";
                case "Register": return "रजिस्टर";
                case "Toggle Language": return "भाषा टॉगल करें";
                case "Login successful for: ": return "के लिए लॉगिन सफल: ";
                case "Redirecting to Admin Dashboard": return "एडमिन डैशबोर्ड पर रीडायरेक्ट हो रहा है";
                case "Redirecting to Customer Dashboard": return "कस्टमर डैशबोर्ड पर रीडायरेक्ट हो रहा है";
                case "Invalid email or password": return "अमान्य ईमेल या पासवर्ड";
                case "Login Failed": return "लॉगिन विफल";
                case "Error during login: ": return "लॉगिन के दौरान त्रुटि: ";
                case "Error": return "त्रुटि";
                case "Contact support to reset your password.": return "पासवर्ड रीसेट करने के लिए समर्थन से संपर्क करें।";
                case "Registration feature coming soon!": return "रजिस्ट्रेशन सुविधा जल्द ही आएगी!";
                default: return key;
            }
        }
        return key;
    }

    private String getTranslatedTitle() {
        return currentLanguage.equals("hi") ? "बाइक रेंटल सिस्टम - लॉगिन" : "Bike Rental System - Login";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}