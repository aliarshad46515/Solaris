package Aniqa;

import Solaris.DBConnector;
import org.jdesktop.swingx.JXButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import static java.awt.Font.BOLD;

public class PaymentForm extends JDialog {
    private static JComboBox<String> paymentType;
    private static JTextField cardNumberField;
    private static JTextField expiryDateField;
    private static JTextField cvvField;
    private static JTextField cardHolderField;
    private static int userId;

    public static int getCartID() {
        return cartID;
    }

    private static int cartID;

    public static double getProductPrice() {
        return productPrice;
    }

    private static double productPrice;

    public static int getProductId() {
        return productId;
    }

    public static int getUserId() {
        return userId;
    }

    private static int productId;

    public static JTextField getCardHolderField() {
        return cardHolderField;
    }

    public static JTextField getCvvField() {
        return cvvField;
    }

    public static JTextField getExpiryDateField() {
        return expiryDateField;
    }

    public static JTextField getCardNumberField() {
        return cardNumberField;
    }

    public static JComboBox<String> getPaymentType() {
        return paymentType;
    }

    public PaymentForm() {}

    public PaymentForm(JFrame owner, int cartID, String productName, double productPrice, int userId, int productId) {
        PaymentForm.userId = userId;
        PaymentForm.productId = productId;
        PaymentForm.productPrice = productPrice;
        PaymentForm.cartID = cartID;
        super(owner, "Payment Form", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initializeComponents(productName, productPrice);
        setVisible(true);
    }

    private void initializeComponents(String productName, double productPrice) {
        setSize(900, 500);
        setLayout(new BorderLayout());

        JPanel leftPanel = createLeftPanel(productName, productPrice);
        JPanel rightPanel = createRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private static JPanel createLeftPanel(String productName, double productPrice) {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(250, 500));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel("Make Payment");
        heading.setFont(new Font("Comic Sans MS", BOLD, 18));
        heading.setForeground(Color.BLACK);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subheading = new JLabel(String.format("You are paying total of €%.2f", productPrice));
        subheading.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        subheading.setForeground(Color.DARK_GRAY);
        subheading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productLabel = new JLabel(productName + " - €" + productPrice);
        productLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        productLabel.setForeground(Color.BLACK);
        productLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(heading);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(subheading);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(productLabel);
        leftPanel.add(Box.createVerticalGlue());

        return leftPanel;
    }

    private static JPanel createRightPanel() {
        DBConnector db = new DBConnector();
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        rightPanel.setPreferredSize(new Dimension(600, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel cardTypeLabel = new JLabel("Choose Payment Type:");
        cardTypeLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        paymentType = new JComboBox<>(new String[]{"Credit Card", "iDEAL"});

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        cardNumberField = new JTextField("Enter 16-digit card number");

        JLabel expiryDateLabel = new JLabel("Expiry Date (MM/YY):");
        expiryDateLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        expiryDateField = new JTextField("MM/YY format");

        JLabel cvvLabel = new JLabel("CVC / CVV:");
        cvvLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        cvvField = new JTextField("3 or 4-digit CVV");

        JLabel cardHolderLabel = new JLabel("Cardholder Name:");
        cardHolderLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        cardHolderField = new JTextField("Full name as on card");

        JXButton payButton = new JXButton("Pay");
        payButton.setFont(new Font("Comic Sans MS", BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0;
        rightPanel.add(cardTypeLabel, gbc);
        gbc.gridx = 1;
        rightPanel.add(paymentType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        rightPanel.add(cardNumberLabel, gbc);
        gbc.gridx = 1;
        rightPanel.add(cardNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        rightPanel.add(expiryDateLabel, gbc);
        gbc.gridx = 1;
        rightPanel.add(expiryDateField, gbc);
        gbc.gridx = 2;
        rightPanel.add(cvvLabel, gbc);
        gbc.gridx = 3;
        rightPanel.add(cvvField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        rightPanel.add(cardHolderLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        rightPanel.add(cardHolderField, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 3;
        rightPanel.add(payButton, gbc);
        gbc.gridwidth = 1;

        PaymentForm pf = new PaymentForm();

        pf.addFocusListener(cardNumberField, "Enter 16-digit card number");
        pf.addFocusListener(expiryDateField, "MM/YY format");
        pf.addFocusListener(cvvField, "3 or 4-digit CVV");
        pf.addFocusListener(cardHolderField, "Full name as on card");

        payButton.addActionListener(e -> db.paymentDetails());

        return rightPanel;
    }

    private void addFocusListener(JTextField field, String defaultText) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(defaultText)) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(defaultText);
                }
            }
        });
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new PaymentForm(null, "Cart", 1));
    }
}
