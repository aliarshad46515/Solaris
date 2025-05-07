package Solaris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class CartPage {

    private static final List<String[]> cartItems = new ArrayList<>();
    private static JLabel totalLabel;
    private static JPanel checkoutPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Demo Store - Cart");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // ... (Header)
            JPanel headerPanel = createHeaderPanel();
            frame.add(headerPanel, BorderLayout.NORTH);

            // Initialize totalLabel and checkoutPanel
            totalLabel = new JLabel("Total: €0.00");
            totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
            checkoutPanel = createCheckoutPanel(); // Initialize here

            // Main Content (Split Pane)
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            // Left: Cart Items
            JPanel cartItemsPanel = createCartItemsPanel();
            JScrollPane cartScrollPane = new JScrollPane(cartItemsPanel);
            mainSplitPane.setLeftComponent(cartScrollPane);

            // Right: Checkout Panel (now with product list)
            mainSplitPane.setRightComponent(checkoutPanel);

            mainSplitPane.setDividerLocation(500);
            frame.add(mainSplitPane, BorderLayout.CENTER);

            // ... (Footer)
            JPanel footerPanel = createFooterPanel();
            frame.add(footerPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 153, 84));
        headerPanel.setPreferredSize(new Dimension(800, 50));
        JLabel lblHeader = new JLabel("Demo Store Cart", JLabel.CENTER);
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblHeader, BorderLayout.CENTER);
        return headerPanel;
    }

    private static JPanel createCartItemsPanel() {
        JPanel cartPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[][] products = {
                {"JHipster T-Shirt", "T-Shirt with JHipster logo", "Size: S", "12", "T-Shirt", "src/main/resources/jhipster_tshirt.png"},
                {"JHipster Cap", "Cool JHipster Cap", "One Size", "10", "Cap", "src/main/resources/jhipster_cap.png"},
                {"JHipster Mug", "Morning Coffee Mug", "One Size", "8", "Mug", "src/main/resources/jhipster_mug.png"}
        };

        for (String[] product : products) {
            addItemToCart(product, cartPanel);
        }
        return cartPanel;
    }

    public static void addItemToCart(String[] product, JPanel cartPanel) {
        cartItems.add(product);
        updateTotal();
        JPanel productCard = createProductCard(product);
        cartPanel.add(productCard);
        cartPanel.revalidate();
        cartPanel.repaint();
        updateCheckoutPanel(); // Update the checkout panel
    }

    private static JPanel createProductCard(String[] product){
        JPanel productCard = new JPanel(new FlowLayout(FlowLayout.LEFT));
        productCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // ... (Image and product info code as before)
        JPanel imagePanel = new JPanel();
        try{
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(product[5]).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon);
            imagePanel.add(imageLabel);
        } catch (Exception e){
            System.err.println("Error loading image: " + e.getMessage());
            JLabel noImageLabel = new JLabel("No Image");
            imagePanel.add(noImageLabel);
        }
        productCard.add(imagePanel);

        JPanel productInfo = new JPanel(new GridLayout(0, 1));
        JLabel lblProductName = new JLabel(product[0]);
        lblProductName.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel lblDescription = new JLabel(product[1]);
        JLabel lblSize = new JLabel(product[2]);
        JLabel lblPrice = new JLabel("€ " + product[3]);
        JLabel lblCategory = new JLabel("Category: " + product[4]);

        productInfo.add(lblProductName);
        productInfo.add(lblDescription);
        productInfo.add(lblSize);
        productInfo.add(lblPrice);
        productInfo.add(lblCategory);

        productCard.add(productInfo);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> {
            cartItems.remove(product);
            updateTotal();
            Component parent = productCard.getParent();
//            if (parent != null) {
//                parent.remove(productCard);
//                parent.revalidate();
//                parent.repaint();
//            }
        });
        buttonPanel.add(btnRemove);
        productCard.add(buttonPanel);
        return productCard;
    }


    private static JPanel createCheckoutPanel() {
        JPanel checkoutPanel = new JPanel(new BorderLayout());
        checkoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel productListPanel = new JPanel(new GridLayout(0, 1)); // Panel for product list
        checkoutPanel.add(productListPanel, BorderLayout.CENTER); // Add to center

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);
        checkoutPanel.add(totalPanel, BorderLayout.NORTH);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Checkout clicked!"));
        checkoutPanel.add(checkoutButton, BorderLayout.SOUTH);

        return checkoutPanel;
    }

    private static void updateCheckoutPanel() {
        if (checkoutPanel != null) {
            JPanel productListPanel = (JPanel) checkoutPanel.getComponent(0); // Get the product list panel
            productListPanel.removeAll(); // Clear existing items

            for (String[] item : cartItems) {
                JLabel itemLabel = new JLabel(item[0] + " - €" + item[3]);
                productListPanel.add(itemLabel);
            }

            productListPanel.revalidate();
            productListPanel.repaint();
            updateTotal();
        }
    }

    private static JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setPreferredSize(new Dimension(800, 30));
        JLabel lblFooter = new JLabel("Demo Store © 2024", JLabel.CENTER);
        footerPanel.add(lblFooter);
        return footerPanel;
    }

    private static void updateTotal() {
        double total = 30;
        for (String[] item : cartItems) {
            try {
                total += Double.parseDouble(item[3]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid price format: " + item[3]);
            }
        }
        totalLabel.setText("Total: €");
    }
}