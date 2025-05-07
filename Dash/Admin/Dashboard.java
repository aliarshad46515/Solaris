package Dash.Admin;

import org.jdesktop.swingx.JXButton;
import Charts.*;
import Solaris.*;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
public class Dashboard {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLayout(new BorderLayout());

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(6, 1, 10, 10));
        leftPanel.setBackground(new Color(25, 42, 86));
        leftPanel.setPreferredSize(new Dimension(220, 500));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo with Circular Icon and Text
        ImageIcon logoIcon = new ImageIcon("dropdown.png");
        Image img = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel("Admin Panel", createCircularIcon(new ImageIcon(img)), SwingConstants.CENTER);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Serif", Font.BOLD, 18));
        logoLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        logoLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        leftPanel.add(logoLabel);

        // Buttons
        String[] buttons = {"Dashboard", "Manage Product", "View Sales & Inventory", "Manage Users"};
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);

        // Create panels for each button
        JPanel dashboardPanel = createDashBoardPanel();
        dashboardPanel.add(createDashPie());
        dashboardPanel.add(createDashBar());
        dashboardPanel.add(createDashLine());
        JPanel manageProductPanel = createProductPanel(frame);
        JPanel salesInventoryPanel = createPlaceholderPanel();
        JPanel manageUsersPanel = createPlaceholderPanel();
        manageUsersPanel.add(createUsersPanel(), BorderLayout.CENTER);
        salesInventoryPanel.add(createSalesPanel(), BorderLayout.CENTER);

        // Add panels to the contentPanel with the corresponding names
        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(manageProductPanel, "Manage Product");
        contentPanel.add(salesInventoryPanel, "View Sales & Inventory");
        contentPanel.add(manageUsersPanel, "Manage Users");

        // Create buttons and add action listeners to switch the panels
        for (String text : buttons) {
            JXButton btn = new JXButton(text);
            btn.setBackground(new Color(41, 128, 185));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
            btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addHoverEffect(btn);
            addButtonShadow(btn);
            btn.addActionListener(e -> cardLayout.show(contentPanel, text));
            leftPanel.add(btn);
        }

        // Navbar (Top Panel)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(41, 128, 185));
        topPanel.setPreferredSize(new Dimension(1200, 60));

        // Navbar Left - Menu Icon
        ImageIcon menuIcon = new ImageIcon("dropdown.png");
        Image imgMenu = menuIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel menuLogo = new JLabel(createCircularIcon(new ImageIcon(imgMenu)));
        menuLogo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(menuLogo, BorderLayout.WEST);

        // Navbar Center - Scrolling Text
        JPanel navCenter = new JPanel();
        navCenter.setLayout(null);
        navCenter.setBackground(new Color(41, 128, 185));

        JLabel adminLabel = new JLabel("Welcome to the Admin Panel of the Solar System Management—your one-stop solution for efficiently managing solar panel sales, purchases, user accounts, and product operations, ensuring smooth business workflows.");
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Serif", Font.ITALIC, 18));
        adminLabel.setBounds(1200, 10, 600, 40);
        navCenter.add(adminLabel);

        Timer timer = new Timer(8, new ActionListener() {
            int x = frame.getWidth(); // Start from the right edge of the frame

            @Override
            public void actionPerformed(ActionEvent e) {
                x -= 2; // Move text to the left
                if (x < -adminLabel.getPreferredSize().width) {
                    x = frame.getWidth(); // Reset to the right edge
                }
                adminLabel.setLocation(x, adminLabel.getY());
                adminLabel.repaint();
            }
        });
        timer.start();

        topPanel.add(navCenter, BorderLayout.CENTER);

        String[] loginOptions = {"Login", "Logout"};
        JComboBox<String> comboBox = new JComboBox<>(loginOptions);
        comboBox.setFont(new Font("Serif", Font.PLAIN, 16));
        comboBox.setBackground(new Color(41, 128, 185));
        comboBox.setForeground(Color.WHITE);
        comboBox.setFocusable(false);
        comboBox.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        comboBox.addActionListener(e -> {
            String selectedOption = (String) comboBox.getSelectedItem();
            if ("Logout".equals(selectedOption)) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "You selected: " + selectedOption);
            }
        });
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(41, 128, 185));
        rightPanel.add(comboBox);
        topPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "Dashboard");
        frame.setVisible(true);
    }

    private static JPanel createPlaceholderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        return panel;
    }

    private static JPanel createDashBar() {
        BarGraph barGraph = new BarGraph();
        return barGraph.createBarGraphPanel();
    }

    private static JPanel createDashBoardPanel() {
        return new JPanel(new GridLayout(0, 2, 20, 10));
    }

    private static JPanel createDashPie() {
        PieChart pieChart = new PieChart();
        return pieChart.createPieChartPanel();
    }

    private static JPanel createDashLine() {
        LineChart lineChart = new LineChart();
        return lineChart.createLineChartPanel();
    }

    private static JPanel createProductPanel(JFrame parentFrame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        northPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 20, 0));

        JXButton addButton = new JXButton("Add Product");
        JXButton pricingButton = new JXButton("Pricing");
        JLabel prodLabel = new JLabel("Total Products: ");
        JLabel outProd = new JLabel("Types: ");

        updateInventoryStats(prodLabel, outProd);

        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);

        pricingButton.setBackground(new Color(76, 175, 80));
        pricingButton.setForeground(Color.WHITE);

        northPanel.add(addButton);
        northPanel.add(pricingButton);
        northPanel.add(prodLabel);
        northPanel.add(outProd);
        panel.add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = TablePanel.tablePanel("Admin");
        panel.add(centerPanel, BorderLayout.CENTER);

        pricingButton.addActionListener(e -> {
            ProdCatalog.updatePricing();
        });

        addButton.addActionListener(e -> {
            ProdCatalog.addProduct();
        });

        return panel;
    }

    private static void updateInventoryStats(JLabel totalProductsLabel, JLabel typesLabel) {
        DBConnector db = new DBConnector();

        int totalProducts = db.getTotalProducts();
        int productTypes = db.getProductTypesByOutput();

        totalProductsLabel.setText("Total Products: " + totalProducts);
        typesLabel.setText("Types of Products: " + productTypes);
    }



    private static void addHoverEffect(JXButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
        });
    }

    private static void addButtonShadow(JXButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 2, 5, 2, new Color(0, 0, 0, 100)),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private static ImageIcon createCircularIcon(ImageIcon icon) {
        int size = Math.min(icon.getIconWidth(), icon.getIconHeight());
        BufferedImage bufferedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2d.drawImage(icon.getImage(), 0, 0, size, size, null);
        g2d.dispose();
        return new ImageIcon(bufferedImage);
    }

    private static JPanel createUsersPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(Color.WHITE);

        JLabel loggedUsersLabel = new JLabel("Logged Users: 0", SwingConstants.CENTER);
        JLabel totalUsersLabel = new JLabel("Total Users: 0", SwingConstants.CENTER);

        loggedUsersLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalUsersLabel.setFont(new Font("Arial", Font.BOLD, 14));

        statsPanel.add(loggedUsersLabel);
        statsPanel.add(totalUsersLabel);
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel usersGridPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        usersGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        populateUsers(usersGridPanel, loggedUsersLabel, totalUsersLabel);

        JScrollPane scrollPane = new JScrollPane(usersGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private static void populateUsers(JPanel usersGridPanel, JLabel loggedUsersLabel, JLabel totalUsersLabel) {
        DBConnector db = new DBConnector();

        int totalUsers = db.getTotalUsers();
        int loggedUsers = db.getLoggedUsers();
        ArrayList<User> users = db.getAllUsers();

        totalUsersLabel.setText("Total Users: " + totalUsers);
        loggedUsersLabel.setText("Logged Users: " + loggedUsers);

        usersGridPanel.removeAll();

        for (User user : users) {
            JPanel userPanel = createUserPanel(user);
            usersGridPanel.add(userPanel);
        }

        usersGridPanel.revalidate();
        usersGridPanel.repaint();
    }

    private static JPanel createUserPanel(User user) {
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        userPanel.setPreferredSize(new Dimension(100, 120));

        JLabel nameLabel = new JLabel(user.getUserName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detailsPanel.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email: " + user.getEmail(), SwingConstants.LEFT);
//        JLabel statusLabel = new JLabel("Status: " + (user.isLoggedIn() ? "Online" : "Offline"), SwingConstants.LEFT);

        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        detailsPanel.add(emailLabel);
//        detailsPanel.add(statusLabel);

        userPanel.add(detailsPanel, BorderLayout.CENTER);

        return userPanel;
    }

    private static JPanel createSalesPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 40, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setBackground(Color.WHITE);

        JLabel totalSalesLabel = new JLabel("Total Sales: $0", SwingConstants.LEFT);
        JLabel availableProductsLabel = new JLabel("Available Products: 0", SwingConstants.LEFT);
        JLabel totalInvestmentLabel = new JLabel("Total Investment: $0", SwingConstants.LEFT);
        JLabel totalRevenueLabel = new JLabel("Total Revenue: $0", SwingConstants.LEFT);
        JLabel roiLabel = new JLabel("ROI: 0%", SwingConstants.LEFT);
        JLabel downloadLogLabel = new JLabel("<html><u>Download Log</u></html>", SwingConstants.LEFT);

        Font statsFont = new Font("Arial", Font.BOLD, 14);
        totalSalesLabel.setFont(statsFont);
        availableProductsLabel.setFont(statsFont);
        totalInvestmentLabel.setFont(statsFont);
        totalRevenueLabel.setFont(statsFont);
        roiLabel.setFont(statsFont);
        downloadLogLabel.setFont(statsFont);

        downloadLogLabel.setForeground(Color.BLUE);
        downloadLogLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        statsPanel.add(totalSalesLabel);
        statsPanel.add(availableProductsLabel);
        statsPanel.add(totalInvestmentLabel);
        statsPanel.add(totalRevenueLabel);
        statsPanel.add(roiLabel);
        statsPanel.add(downloadLogLabel);

        mainPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel salesGridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        salesGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        populateSalesData(salesGridPanel, totalSalesLabel, availableProductsLabel, totalInvestmentLabel, totalRevenueLabel, roiLabel);

        JScrollPane scrollPane = new JScrollPane(salesGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        downloadLogLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                downloadSalesLog();
            }
        });

        return mainPanel;
    }

    private static void populateSalesData(JPanel salesGridPanel, JLabel totalSalesLabel, JLabel availableProductsLabel,
                                          JLabel totalInvestmentLabel, JLabel totalRevenueLabel, JLabel roiLabel) {
        DBConnector db = new DBConnector();

        int availableProducts = db.getTotalProducts();
        double totalInvestment = db.getTotalInvestment();
        double totalRevenue = db.getTotalRevenue();
        double totalSales = db.getTotalSales();
        ArrayList<Product> soldProducts = db.getSoldProducts();

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Color labelColor = new Color(51, 51, 51);
        Color valueColor = new Color(0, 102, 204);

        applyLabelStyle(availableProductsLabel, labelFont, labelColor, valueColor, "Available Products:", String.valueOf(availableProducts));
        applyLabelStyle(totalInvestmentLabel, labelFont, labelColor, valueColor, "Total Investment:", "$" + String.format("%.2f", totalInvestment));
        applyLabelStyle(totalRevenueLabel, labelFont, labelColor, valueColor, "Total Revenue:", "$" + String.format("%.2f", totalRevenue));
        applyLabelStyle(totalSalesLabel, labelFont, labelColor, valueColor, "Total Sales:", "$" + String.format("%.2f", totalSales));

        double roi = (totalRevenue - totalInvestment) / totalInvestment * 100;
        applyLabelStyle(roiLabel, labelFont, labelColor, valueColor, "ROI:", String.format("%.2f", roi) + "%");

        salesGridPanel.removeAll();
        salesGridPanel.setLayout(new GridLayout(0, 4, 10, 10));

        for (Product product : soldProducts) {
            JPanel productPanel = createSalesProductPanel(product);
            productPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            salesGridPanel.add(productPanel);
        }

        roiLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showDatePicker.showDatePickerDialog();
            }
        });

        salesGridPanel.revalidate();
        salesGridPanel.repaint();
    }

    private static void applyLabelStyle(JLabel label, Font font, Color labelColor, Color valueColor, String labelText, String value) {
        label.setFont(font);
        label.setForeground(labelColor);
        label.setText("<html><font color='" + labelColor.getRGB() + "'>" + labelText + "</font> <font color='" + valueColor.getRGB() + "'>" + value + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private static JPanel createSalesProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        productPanel.setPreferredSize(new Dimension(200, 250));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 150));
        if (product.getImgURLs() != null) {
            ImageIcon imageIcon = new ImageIcon((product.getImgURLs())[0]);
            Image scaledImage = imageIcon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("No Image");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        productPanel.add(imageLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(product.getProdName(), SwingConstants.CENTER);
        JLabel priceLabel = new JLabel("Price: $" + product.getProdPrice(), SwingConstants.CENTER);
        JLabel soldQuantityLabel = new JLabel("Sold: " + product.getProdQuantity(), SwingConstants.CENTER);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        soldQuantityLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        detailsPanel.add(nameLabel);
        detailsPanel.add(priceLabel);
        detailsPanel.add(soldQuantityLabel);

        productPanel.add(detailsPanel, BorderLayout.CENTER);

        return productPanel;
    }



    private static void downloadSalesLog() {
        JOptionPane.showMessageDialog(null, "Sales log downloaded successfully.", "Download Log", JOptionPane.INFORMATION_MESSAGE);
    }
}

