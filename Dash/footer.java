package Aniqa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class footer {
    static JFrame frame;

    public static void createFooter(JComponent parentPanel) {
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Top description panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.white);
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel textPanel = new JPanel();
        textPanel.setBackground(Color.white);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(createLabel("Grocery/Supermart", 20, Color.BLACK, true));
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(createLabel(
                "Launching into the grocery vertical, Flipkart introduces Supermart.\n" +
                        "Explore a wide variety of products from essentials to cleaning agents with convenience.",
                16, Color.DARK_GRAY, false
        ));

        JButton infoButton = new JButton("More Info");
        infoButton.setPreferredSize(new Dimension(120, 30));
        infoButton.setBackground(new Color(70, 130, 180));
        infoButton.setForeground(Color.WHITE);
        infoButton.setFocusPainted(false);
        infoButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        infoButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "More details coming soon!"));

        topPanel.add(textPanel, BorderLayout.CENTER);
        topPanel.add(infoButton, BorderLayout.EAST);

        // Bottom panel with sections
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(15, 15, 30)); // Dark blue color

        bottomPanel.setLayout(new GridLayout(1, 5, 20, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        bottomPanel.add(createSection("ABOUT", new String[]{
                "Contact Us", "About Us", "Careers", "Flipkart Stories", "Press", "Corporate Information"
        }));
        bottomPanel.add(createSection("GROUP COMPANIES", new String[]{
                "Myntra", "Cleartrip", "Shopsy"
        }));
        bottomPanel.add(createSection("HELP", new String[]{
                "Payments", "Shipping", "Cancellation & Returns", "FAQ"
        }));
        bottomPanel.add(createSection("CONSUMER POLICY", new String[]{
                "Cancellation & Returns", "Terms Of Use", "Security", "Privacy", "Sitemap"
        }));
        bottomPanel.add(createContactSection());

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(bottomPanel, BorderLayout.CENTER);
        parentPanel.add(contentPanel, BorderLayout.SOUTH);
    }

    private static JPanel createSection(String title, String[] links) {
        JPanel section = new JPanel();
        section.setBackground(new Color(15, 15, 30)); // Dark blue color

        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.add(createLabel(title, 16, Color.WHITE, true));
        section.add(Box.createRigidArea(new Dimension(0, 10)));

        for (String link : links) {
            JLabel linkLabel = createLabel(link, 14, Color.LIGHT_GRAY, false);
            linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            linkLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    linkLabel.setForeground(Color.WHITE);
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    linkLabel.setForeground(Color.LIGHT_GRAY);
                }
            });
            section.add(linkLabel);
        }
        return section;
    }

    private static JPanel createContactSection() {
        JPanel contactSection = new JPanel();
        contactSection.setBackground(new Color(15, 15, 30)); // Dark blue color

        contactSection.setLayout(new BoxLayout(contactSection, BoxLayout.Y_AXIS));
        contactSection.add(createLabel("Mail Us:", 16, Color.WHITE, true));
        contactSection.add(Box.createRigidArea(new Dimension(0, 10)));
        contactSection.add(createLabel("Enter your email:", 14, Color.WHITE, false));

        JTextField emailField = new JTextField("example@domain.com");
        emailField.setPreferredSize(new Dimension(200, 25));
        emailField.setMaximumSize(new Dimension(200, 25));
        emailField.setForeground(Color.GRAY);
        emailField.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (emailField.getText().equals("example@domain.com")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(Color.GRAY);
                    emailField.setText("example@domain.com");
                }
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 30));
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        submitButton.addActionListener(e -> {
            String email = emailField.getText();
            if (validateEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Email Submitted Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Email Address!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contactSection.add(Box.createRigidArea(new Dimension(0, 10)));
        contactSection.add(emailField);
        contactSection.add(Box.createRigidArea(new Dimension(0, 10)));
        contactSection.add(submitButton);

        return contactSection;
    }

    private static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    private static JLabel createLabel(String text, int fontSize, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Comic Sans MS", bold ? Font.BOLD : Font.PLAIN, fontSize));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
