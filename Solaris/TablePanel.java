package Solaris;

import org.jdesktop.swingx.JXButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TablePanel {

    static JPanel panel;

    public static void main(String[] args) {
        JFrame parentFrame = new JFrame("Main Frame");
        parentFrame.setSize(400, 300);
        parentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentFrame.setVisible(true);

        JDialog productDialog = createProductDialog(parentFrame);
        productDialog.setVisible(true);
    }


    public static JPanel tablePanel(String line) {
        DBConnector db = new DBConnector();
        JScrollPane scrollPane;
        if(line.equals("Admin")) {
            scrollPane = db.getProduct();
            scrollPane.setBorder(null);
        } else {
            scrollPane = db.getCartProducts(4);
            scrollPane.setBorder(null);
        }

        panel = new JPanel(new BorderLayout(20, 20));
        JLabel prodLabel = new JLabel("Products", SwingConstants.CENTER);
        prodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(prodLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JDialog createProductDialog(JFrame parentFrame) {
        JDialog productDialog = new JDialog(parentFrame, "Product List", true);
        productDialog.setSize(500, 400);
        productDialog.setLocationRelativeTo(parentFrame);

        JPanel productPanel = tablePanel("Cart");

        productDialog.add(productPanel);

        JXButton closeButton = new JXButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.addActionListener(e -> productDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        productDialog.setLayout(new BorderLayout(10, 10));
        productDialog.add(productPanel, BorderLayout.CENTER);
        productDialog.add(buttonPanel, BorderLayout.SOUTH);

        return productDialog;
    }


    public static void refreshTablePanel() {
        panel.removeAll();
        JScrollPane scrollPane = new DBConnector().getProduct();
        scrollPane.setBorder(null);

        JLabel prodLabel = new JLabel("Products", SwingConstants.CENTER);
        prodLabel.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(prodLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }


    public static JPanel createProductPanel(int productID, String[] imagePath, String productName, String manufacturer, Double productPrice,
                                            String productDes, String productQuantity, String productCat, String output) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        productPanel.setPreferredSize(new Dimension(500, 200));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 200));
        ImageIcon imageIcon = new ImageIcon(imagePath[0]);
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(image));
        productPanel.add(imageLabel, BorderLayout.WEST);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(3, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Name: " + productName);
        JLabel manufacturerLabel = new JLabel("Manufacturer: " + manufacturer);
        JLabel priceLabel = new JLabel("Price: $" + productPrice);

        detailsPanel.add(nameLabel);
        detailsPanel.add(manufacturerLabel);
        detailsPanel.add(priceLabel);

        productPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

        JXButton editButton = createButton("Edit");
        JXButton deleteButton = createButton("Delete");

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        editButton.addActionListener(e -> {
            updateProd(productID, imagePath, productName, manufacturer, productPrice, productDes, productQuantity, productCat, output);
        });

        deleteButton.addActionListener(e -> {
            deleteProd(productID);
        });

        productPanel.add(buttonPanel, BorderLayout.EAST);
        productPanel.setBorder(null);

        return productPanel;
    }

    private static void deleteProd(int productID) {
        DBConnector db = new DBConnector();
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this product?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deleteProduct(productID)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Product deleted successfully.",
                        "Deletion Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshTablePanel();
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to delete product. Please check the logs for details.",
                        "Deletion Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private static JXButton createButton(String line) {
        JXButton button = new JXButton(line);
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(30, 120, 220));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(50, 150, 250));
            }
        });
        return button;
    }

    public static void updateProd(int productID, String[] imagePath, String productName, String manufacturer, Double productPrice,
                                  String productDes, String productQuantity, String productCat, String output) {
        UpdProductDialog update = new UpdProductDialog();

        Product prod = new Product(productID, productName, productDes, String.valueOf(productPrice), productQuantity, productCat, manufacturer, output);
        prod.setImgURLs(imagePath);

        update.updateProduct(prod);
        refreshTablePanel();
    }
}
