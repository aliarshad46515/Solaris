package Dash.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class adminLogin{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Login");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Centering the frame
        frame.setLocationRelativeTo(null);

        // Setting up the main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Adding company name
        JLabel companyName = new JLabel(" The Solar System", SwingConstants.CENTER);
        companyName.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(companyName, gbc);

        // Admin name label and field
        JLabel adminLabel = new JLabel("Admin Name:");
        adminLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(adminLabel, gbc);

        JTextField adminField = new JTextField(); // Default admin name as capital "Admin"
        adminField.setFont(new Font("Serif", Font.PLAIN, 14));
        adminField.setPreferredSize(new Dimension(250, 30));
        adminField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        adminField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String text = adminField.getText();
                if (!text.isEmpty() && !Character.isUpperCase(text.charAt(0))) {
                    adminField.setText(text.substring(0, 1).toUpperCase() + text.substring(1));
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(adminField, gbc);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Serif", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(250, 30));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Checkbox to show/hide password
        JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("Serif", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(showPasswordCheckBox, gbc);

        // Message label for password validation
        JLabel passwordMessage = new JLabel();
        passwordMessage.setFont(new Font("Serif", Font.PLAIN, 12));
        passwordMessage.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(passwordMessage, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Serif", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(0, 90, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(0, 123, 255));
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        // Adding action listener to login button
        boolean[] successfulLoginShown = {false}; // Flag to prevent multiple success dialogs
        loginButton.addActionListener(e -> {
            String adminName = adminField.getText();
            String password = new String(passwordField.getPassword());

            // Resetting password message
            passwordMessage.setText("");

            // Check for empty fields
            if (adminName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{12,}$")) {
                // Displaying password format error within the form
                passwordMessage.setText("Pass contains 12 chars include letters, nums & special char!");
            } else {
                // Displaying processing effect within the form
                JLabel processingLabel = new JLabel("Processing...");
                processingLabel.setFont(new Font("Serif", Font.BOLD, 16));
                processingLabel.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridx = 0;
                gbc.gridy = 6;
                gbc.gridwidth = 2;
                mainPanel.add(processingLabel, gbc);
                frame.revalidate();
                frame.repaint();

                // Simulating processing delay
                new Timer(2000, evt -> {
                    // Remove processing label and show success message only once
                    mainPanel.remove(processingLabel);
                    frame.revalidate();
                    frame.repaint();

                    // Show success dialog (only once)
                    if (!successfulLoginShown[0]) {
                        JOptionPane.showMessageDialog(frame, "Successful Login", "Success", JOptionPane.INFORMATION_MESSAGE);
                        successfulLoginShown[0] = true;
                    }

                    // Close the login window
                    frame.dispose();
                }).start();
            }
        });

        // Password show/hide functionality
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0); // Show password
            } else {
                passwordField.setEchoChar('*'); // Hide password
            }
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
