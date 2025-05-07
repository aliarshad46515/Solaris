package Solaris;

import Aniqa.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import Products.*;
import java.time.format.*;
import org.jdesktop.swingx.JXButton;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class DBConnector {
    private Connection con;

    void connectDB() throws Exception {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Solaris", "root", "");
            System.out.println(con);
            System.out.println("DB Connected Successfully to: " + con.getCatalog());

    }

    boolean validateLogin(String username, String password) {
        try {
            connectDB();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM User WHERE Username = ? AND Userpass = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error!\n" + e);
            return false;
        }
    }

    boolean addProd(String title, String desc, String price, String cat, String quantity, String[] urls) {
        try {
            connectDB();

            String sql = "INSERT INTO Products (prodName, prodDes, prodPrice, prodCategory, prodQuantity) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement stmt = con.prepareStatement(sql);
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, title);
            stmt.setString(2, desc);
            stmt.setString(3, price);
            stmt.setString(4, cat);
            stmt.setString(5, quantity);

            stmt.executeUpdate();

            if (urls != null && urls.length > 0) {
                String imageSql = "INSERT INTO ProductImages (prodID, imgURL) VALUES (?, ?)";
                PreparedStatement imageStmt = con.prepareStatement(imageSql);

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int productID = rs.getInt(1);

                    for (String url : urls) {
                        imageStmt.setInt(1, productID);
                        imageStmt.setString(2, url);
                        imageStmt.addBatch();
                    }

                    imageStmt.executeBatch();
                } else {
                    return false;
                }
                imageStmt.close();
            }
            stmt.close();
            return true;
        } catch (Exception e) {
            System.out.println("Error!\n" + e);
            return false;
        }
    }

    boolean updateProduct(int prodID, String title, String desc, String price, String cat, String quantity, String manufacturer, String output, String[] urls) {
        try {
            connectDB();

            String sql = "UPDATE Products SET prodName = ?, prodDes = ?, prodPrice = ?, prodCategory = ?, prodQuantity = ?, manufacturer = ?, output = ? WHERE prodID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, title);
            stmt.setString(2, desc);
            stmt.setString(3, price);
            stmt.setString(4, cat);
            stmt.setString(5, quantity);
            stmt.setString(6, manufacturer);
            stmt.setString(7, output);
            stmt.setInt(8, prodID);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0 && urls != null && urls.length > 0) {
                String deleteSql = "DELETE FROM ProductImages WHERE prodID = ?";
                PreparedStatement deleteStmt = con.prepareStatement(deleteSql);
                deleteStmt.setInt(1, prodID);
                deleteStmt.executeUpdate();
                deleteStmt.close();

                String imageSql = "INSERT INTO ProductImages (prodID, imgURL) VALUES (?, ?)";
                PreparedStatement imageStmt = con.prepareStatement(imageSql);

                for (String url : urls) {
                    imageStmt.setInt(1, prodID);
                    imageStmt.setString(2, url);
                    imageStmt.addBatch();
                }

                imageStmt.executeBatch();
                imageStmt.close();
            }

            stmt.close();
            return rowsUpdated > 0;
        } catch (Exception e) {
            System.out.println("Error!\n" + e);
            return false;
        }
    }

    boolean deleteProduct(int prodID) {
        try {
            connectDB();
            con.setAutoCommit(false); // Start transaction

            try (PreparedStatement deletePaymentsStmt = con.prepareStatement("DELETE FROM Payment WHERE productID = ?")) {
                deletePaymentsStmt.setInt(1, prodID);
                deletePaymentsStmt.executeUpdate();
            }

            try (PreparedStatement deleteImagesStmt = con.prepareStatement("DELETE FROM ProductImages WHERE prodID = ?")) {
                deleteImagesStmt.setInt(1, prodID);
                deleteImagesStmt.executeUpdate();
            }

            try (PreparedStatement deleteProductStmt = con.prepareStatement("DELETE FROM Products WHERE prodID = ?")) {
                deleteProductStmt.setInt(1, prodID);
                int rowsDeleted = deleteProductStmt.executeUpdate();
                if (rowsDeleted > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            }

        } catch (Exception e) {
            System.err.println("Database Error deleting product:\n" + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Error closing connection:\n" + ex.getMessage());
            }
        }
    }

    public JScrollPane getProduct() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2, 10, 10));
        mainPanel.setBorder(null);

        try {
            connectDB();

            PreparedStatement stmt = con.prepareStatement("Select * from products");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("prodID");
                String productName = rs.getString("prodName");
                String productDes = rs.getString("prodDes");
                String productQuantity = rs.getString("prodQuantity");
                String productCat = rs.getString("prodCategory");
                String manufacturer = rs.getString("manufacturer");
                Double productPrice = rs.getDouble("prodPrice");
                String output = rs.getString("output");


                String[] imagePath = new String[3];
                int imageIndex = 0;
                PreparedStatement stmt2 = con.prepareStatement("Select imgURL from productImages where prodID = ?");
                stmt2.setInt(1, productId);
                ResultSet rs2 = stmt2.executeQuery();
                while (rs2.next()) {
                    imagePath[imageIndex] = rs2.getString("imgURL");
                    imageIndex++;
                }
                JPanel productPanel = TablePanel.createProductPanel(productId, imagePath, productName, manufacturer, productPrice, productDes, productQuantity, productCat, output);
                mainPanel.add(productPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JScrollPane(mainPanel);
    }

    public JScrollPane getCartProducts(int userID) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.LIGHT_GRAY);

        try {
            connectDB();

            PreparedStatement stmt = con.prepareStatement(
                    "SELECT p.prodID, p.prodName, p.prodPrice, c.cartID, c.userID " +
                            "FROM products p " +
                            "JOIN cart c ON c.productID = p.prodID " +
                            "WHERE c.userID = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int cartID = rs.getInt("cartID");
                int productId = rs.getInt("prodID");
                String productName = rs.getString("prodName");
                Double productPrice = rs.getDouble("prodPrice");

                String imagePath = null;
                try (PreparedStatement stmt2 = con.prepareStatement(
                        "SELECT imgURL FROM productImages WHERE prodID = ? LIMIT 1")) {
                    stmt2.setInt(1, productId);
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        imagePath = rs2.getString("imgURL");
                    }
                }

                JPanel productPanel = new JPanel(new BorderLayout());
                productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                JLabel imageLabel = new JLabel();
                if (imagePath != null) {
                    try {
                        BufferedImage image = ImageIO.read(new File(imagePath));
                        if (image != null) {
                            Image scaledImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                        } else {
                            imageLabel.setText("Image Not Found");
                        }
                    } catch (IOException ex) {
                        imageLabel.setText("Error Loading Image");
                        ex.printStackTrace();
                    }
                } else {
                    imageLabel.setText("No Image");
                }
                productPanel.add(imageLabel, BorderLayout.WEST);

                JPanel textPanel = new JPanel(new GridLayout(2, 1));
                JLabel nameLabel = new JLabel(productName);
                nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 14));
                textPanel.add(nameLabel);

                JLabel priceLabel = new JLabel("$" + String.format("%.2f", productPrice));
                priceLabel.setFont(new Font("Sans Serif", Font.PLAIN, 12));
                textPanel.add(priceLabel);
                textPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
                productPanel.add(textPanel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JXButton removeButton = new JXButton("Remove");
                JXButton buyButton = new JXButton("Buy");
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

                buttonPanel.add(buyButton);
                buttonPanel.add(removeButton);

                buyButton.addActionListener(e -> {
                    try {
                        PaymentForm paymentForm = new PaymentForm(null, cartID, productName, productPrice, userID, productId);
                        JScrollPane newScrollPane = getCartProducts(userID);
                        Component parent = mainPanel.getParent();
                        if (parent instanceof JViewport) {
                            JScrollPane scrollPane = (JScrollPane) parent.getParent();
                            scrollPane.setViewportView(newScrollPane.getViewport().getView());
                            scrollPane.revalidate();
                            scrollPane.repaint();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error removing from cart", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                removeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            removeFromCart(cartID, userID);
                            JScrollPane newScrollPane = getCartProducts(userID);
                            Component parent = mainPanel.getParent();
                            if (parent instanceof JViewport) {
                                JScrollPane scrollPane = (JScrollPane) parent.getParent();
                                scrollPane.setViewportView(newScrollPane.getViewport().getView());
                                scrollPane.revalidate();
                                scrollPane.repaint();
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Error removing from cart", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                productPanel.add(buttonPanel, BorderLayout.EAST);

                productPanel.setBackground(Color.WHITE);
                productPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

                mainPanel.add(productPanel);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return scrollPane;
    }

    private void removeFromCart(int cartID, int userID) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM cart WHERE cartID = ? AND userID = ?")) {
            stmt.setInt(1, cartID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
    }

    public void paymentDetails() {
        JComboBox<String> paymentType = PaymentForm.getPaymentType();
        JTextField cardNumberField = PaymentForm.getCardNumberField();
        JTextField expiryDateField = PaymentForm.getExpiryDateField();
        JTextField cvvField = PaymentForm.getCvvField();
        JTextField cardHolderField = PaymentForm.getCardHolderField();
        String paymentMethod = (String) paymentType.getSelectedItem();
        String cardNumber = cardNumberField.getText();
        String expiryDate = expiryDateField.getText();
        String cvv = cvvField.getText();
        String cardHolder = cardHolderField.getText();

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || cardHolder.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            connectDB();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/YY");
//            try {
//                LocalDate.parse("01/" + expiryDate, formatter);
//            } catch (DateTimeParseException e) {
//                JOptionPane.showMessageDialog(null, "Invalid expiry date format. Use MM/YY.", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }

            con.setAutoCommit(false);

            try {
                String paymentsQuery = "INSERT INTO payment (payment_type, card_number, payment_amount, expiry_date, cvv, card_holder, paymentDate, userID, productID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement paymentsStmt = con.prepareStatement(paymentsQuery, Statement.RETURN_GENERATED_KEYS);
                paymentsStmt.setString(1, paymentMethod);
                paymentsStmt.setString(2, cardNumber);
                paymentsStmt.setDouble(3, PaymentForm.getProductPrice());
                paymentsStmt.setString(4, expiryDate);
                paymentsStmt.setString(5, cvv);
                paymentsStmt.setString(6, cardHolder);
                paymentsStmt.setDate(7, Date.valueOf(LocalDate.now()));
                paymentsStmt.setInt(8, PaymentForm.getUserId());
                paymentsStmt.setInt(9, PaymentForm.getProductId());

                int paymentsRowsInserted = paymentsStmt.executeUpdate();

                int paymentId;
                try (ResultSet generatedKeys = paymentsStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paymentId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating payment failed, no ID obtained.");
                    }
                }

                if (paymentsRowsInserted > 0) {
                    String cartDeleteQuery = "DELETE FROM cart WHERE userID = ? AND cartID = ?";
                    try (PreparedStatement cartDeleteStmt = con.prepareStatement(cartDeleteQuery)) {
                        cartDeleteStmt.setInt(1, PaymentForm.getUserId());
                        cartDeleteStmt.setInt(2, PaymentForm.getCartID());
                        cartDeleteStmt.executeUpdate();
                    }
                }

                con.commit();
                JOptionPane.showMessageDialog(null, "Order Confirmed!", "Success", JOptionPane.INFORMATION_MESSAGE);
//                dispose();
            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public ArrayList<JPanel> getProductCard() {
        ArrayList<JPanel> productCards = new ArrayList<>();
        try {
            connectDB();

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM products");
            while (resultSet.next()) {
                int id = resultSet.getInt("prodID");
                String name = resultSet.getString("prodName");
                String price = "Rs." + resultSet.getString("prodPrice");
                String discount = resultSet.getString("prodDiscount");
                String description = resultSet.getString("prodDes");
                String manufacturer = resultSet.getString("manufacturer");
                String output = resultSet.getString("output");

                PreparedStatement stmnt = con.prepareStatement("Select imgURL from productimages where prodID = ?");
                stmnt.setInt(1, id);
                ResultSet rs = stmnt.executeQuery();

                ImageIcon icon = new ImageIcon("");
                while (rs.next()) {
                    String path = rs.getString("imgURL");
                    if(!path.isEmpty()) {
                        icon = new ImageIcon(path);
                        System.out.println(path);
                    }
                }
                productCards.add(ProductList.createProductCard(name, price, discount, description, manufacturer, output, icon));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productCards;
    }

    public ArrayList<JPanel> searchProducts(String searchTerm) {
        ArrayList<JPanel> prodCards = new ArrayList<>();

        try {
            connectDB();

            String sql = "SELECT * FROM Products WHERE prodName LIKE ? OR prodDes LIKE ? OR prodCategory LIKE ? OR manufacturer LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, "%" + searchTerm + "%");
            stmt.setString(2, "%" + searchTerm + "%");
            stmt.setString(3, "%" + searchTerm + "%");
            stmt.setString(4, "%" + searchTerm + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("prodID");
                String name = rs.getString("prodName");
                String price = "Rs." + rs.getString("prodPrice");
                String discount = rs.getString("prodDiscount");
                String description = rs.getString("prodDes");
                String manufacturer = rs.getString("manufacturer");
                String output = rs.getString("output");

                PreparedStatement stmnt = con.prepareStatement("Select imgURL from productimages where prodID = ?");
                stmnt.setInt(1, id);
                ResultSet rs1 = stmnt.executeQuery();

                ImageIcon icon = new ImageIcon("");
                while (rs1.next()) {
                    String path = rs1.getString("imgURL");
                    if(!path.isEmpty()) {
                        icon = new ImageIcon(path);
                        System.out.println(path);
                    }
                }
                prodCards.add(ProductList.createProductCard(name, price, discount, description, manufacturer, output, icon));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return prodCards;
    }

    public ArrayList<JPanel> searchProdPrice(String prodPrice) {
        ArrayList<JPanel> prodCards = new ArrayList<>();

        try {
            connectDB();

            String sql = "SELECT * FROM Products WHERE prodPrice < ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, prodPrice);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("prodID");
                String name = rs.getString("prodName");
                String price = "Rs." + rs.getString("prodPrice");
                String discount = rs.getString("prodDiscount");
                String description = rs.getString("prodDes");
                String manufacturer = rs.getString("manufacturer");
                String output = rs.getString("output");

                PreparedStatement stmnt = con.prepareStatement("Select imgURL from productimages where prodID = ?");
                stmnt.setInt(1, id);
                ResultSet rs1 = stmnt.executeQuery();

                ImageIcon icon = new ImageIcon("");
                while (rs1.next()) {
                    String path = rs1.getString("imgURL");
                    if(!path.isEmpty()) {
                        icon = new ImageIcon(path);
                        System.out.println(path);
                    }
                }
                prodCards.add(ProductList.createProductCard(name, price, discount, description, manufacturer, output, icon));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return prodCards;
    }

//    public ArrayList<JPanel> getSorting(String sort) {
//        ArrayList<JPanel> prodCards = new ArrayList<>();
//
//        try {
//            connectDB();
//            switch (sort){
//                case "Price: Low to High":
//                    sortProducts("SELECT * FROM Products ORDER BY prodPrice asc");
//                    break;
//                case "Price: High to Low":
//                    sortProducts("SELECT * FROM Products ORDER BY prodPrice desc");
//                    break;
//                case "Customer Reviews":
//                    sortProducts("SELECT * FROM Products ORDER BY prodReviews desc");
//                    break;
//            }
//            String sql = "SELECT * FROM Products WHERE prodPrice < ?";
//            PreparedStatement stmt = con.prepareStatement(sql);
//
//            stmt.setString(1, prodPrice);
//
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                int id = rs.getInt("prodID");
//                String name = rs.getString("prodName");
//                String price = "Rs." + rs.getString("prodPrice");
//                String discount = rs.getString("prodDiscount");
//
//                PreparedStatement stmnt = con.prepareStatement("Select imgURL from productimages where prodID = ?");
//                stmnt.setInt(1, id);
//                ResultSet rs1 = stmnt.executeQuery();
//
//                ImageIcon icon = new ImageIcon("");
//                while (rs1.next()) {
//                    String path = rs1.getString("imgURL");
//                    if(!path.isEmpty()) {
//                        icon = new ImageIcon(path);
//                        System.out.println(path);
//                    }
//                }
//                prodCards.add(ProductList.createProductCard(name, price, discount, icon));
//            }
//            rs.close();
//            stmt.close();
//        } catch (Exception e) {
//            System.err.println("Database error: " + e.getMessage());
//        }
//
//        return prodCards;
//    }
//
//    private void sortProducts(String sqlQuery) {
//    }

    public void prodChart(DefaultPieDataset dataset) {
        try {
            connectDB();
            String sql = "SELECT manufacturer, COUNT(*) AS product_count FROM products GROUP BY manufacturer";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String manufacturer = resultSet.getString("manufacturer");
                int productCount = resultSet.getInt("product_count");
                dataset.setValue(manufacturer, productCount);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void getDatasetFromDatabase(DefaultCategoryDataset dataset) {
        try{
            connectDB();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT EXTRACT(MONTH FROM paymentDate) AS month, COUNT(*) AS total_products "
                     + "FROM payment GROUP BY EXTRACT(MONTH FROM paymentDate) ORDER BY month ASC");
            while (resultSet.next()) {
                int month = resultSet.getInt("month");
                double sales = resultSet.getDouble("total_products");
                dataset.addValue(sales, "Total Products Sold", getMonthName(month));
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public void getUserRegistrationDataset(DefaultCategoryDataset dataset){
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");

        try{
            connectDB();
            PreparedStatement statement = con.prepareStatement("SELECT MONTH(join_date) AS monthNum, COUNT(*) AS userCount FROM user GROUP BY monthNum ORDER BY monthNum");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int monthNum = resultSet.getInt("monthNum");
                int userCount = resultSet.getInt("userCount");

                dataset.addValue(userCount, "Users Registered", getMonthName(monthNum));
                System.out.println(dataset);
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private String getMonthName(int month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month - 1];
    }


    public static void main(String[] args) {
        DBConnector db = new DBConnector();
    }

    public int getTotalProducts() {
        int count = 0;
        try {
            connectDB();
            PreparedStatement stmnt = con.prepareStatement("Select Count(*) from products");
            ResultSet rs = stmnt.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getTotalUsers() {
        int count = 0;
        try {
            connectDB();
            PreparedStatement stmnt = con.prepareStatement("Select Count(*) from user");
            ResultSet rs = stmnt.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getLoggedUsers() {
        int count = 0;
        try {
            connectDB();
            PreparedStatement stmnt = con.prepareStatement("Select Count(*) from loggedusers");
            ResultSet rs = stmnt.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getProductTypesByOutput() {
        int count = 0;
        try {
            connectDB();
            PreparedStatement stmnt = con.prepareStatement("SELECT COUNT(DISTINCT output) FROM products");
            ResultSet rs = stmnt.executeQuery();
            if(rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean insertLogEntry(String userName) {
        try {
            connectDB();
            String sql = "INSERT INTO loggedusers (userName) VALUES (?)";
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                statement.setString(1, userName);

                int rowsInserted = statement.executeUpdate();
                return rowsInserted > 0;
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {
            connectDB();
            String sql = "SELECT * FROM user";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    int userId = rs.getInt("userID");
                    String userName = rs.getString("userName");
                    String email = rs.getString("Email");

                    User user = new User(userId, userName, email);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
        return users;
    }

    public double getTotalInvestment() {
        double totalInvestment = 0.0;
        try {
            connectDB();
            String sql = "SELECT SUM(Price * Quantity) AS TotalValue FROM Products";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    totalInvestment = rs.getDouble("TotalValue");
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return -1;
        }
        return totalInvestment;
    }

    public double getTotalRevenue() {
        double totalRevenue = 0.0;
        try {
            connectDB();
            String sql = "SELECT SUM(salePrice) AS TotalRevenue FROM payment";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    totalRevenue = rs.getDouble("TotalRevenue");
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return -1;
        }
        return totalRevenue;
    }

    public int getTotalSales() {
        int totalSales = 0;
        try {
            connectDB();
            String sql = "SELECT COUNT(*) AS TotalSales FROM payment";
            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    totalSales = rs.getInt("TotalSales");
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return -1;
        }
        return totalSales;
    }

    public HashMap<Integer, String[]> getProductImagesByProductID() {
        HashMap<Integer, ArrayList<String>> productImagesMap = new HashMap<>();
        try {
            connectDB();
            String sql = "SELECT prodID, imgURL FROM productimages";

            try (PreparedStatement statement = con.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    int prodID = rs.getInt("prodID");
                    String prodImage = rs.getString("imgURL");

                    productImagesMap.computeIfAbsent(prodID, k -> new ArrayList<>()).add(prodImage);
                }
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
        HashMap<Integer, String[]> productImages= new HashMap<>();
        for (HashMap.Entry<Integer, ArrayList<String>> entry : productImagesMap.entrySet()) {
            Integer key = entry.getKey();
            ArrayList<String> value = entry.getValue();
            productImages.put(key, value.toArray(new String[0]));
        }
        return productImages;
    }

    public ArrayList<Product> getSoldProducts() {
        ArrayList<Product> soldProducts = new ArrayList<>();
        HashMap<Integer, String[]> productImagesMap = getProductImagesByProductID();
        try {
            connectDB();
            String sql = "SELECT py.productID, p.prodName, py.salePrice, py.paymentDate " +
                    "FROM payment py " +
                    "INNER JOIN Products p ON py.productID = p.prodID ";

            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("prodName");
                double salePrice = rs.getDouble("salePrice");
                Date paymentDate = rs.getDate("paymentDate");
                String[] images = productImagesMap.getOrDefault(productID, new String[0]);

                Product soldProduct = new Product(productID, productName, salePrice, paymentDate);
                soldProduct.setImgURLs(images);
                soldProducts.add(soldProduct);
            }
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
        return soldProducts;
    }

}