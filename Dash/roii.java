import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class roii {

    public static double calculateROI(double totalSavings, double totalInvestment) {
        if (totalInvestment == 0) return 0;
        return ((totalSavings - totalInvestment) / totalInvestment) * 100;
    }

    public static void populateFields(JTextField investmentField, JTextField savingsField) {
        String url = "jdbc:mysql://localhost:3306/roi";
        String user = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT " +
                    "SUM(CASE WHEN productType = 'purchase' THEN solarprice ELSE 0 END) AS TotalInvestment, " +
                    "SUM(CASE WHEN productType = 'sale' THEN solarprice ELSE 0 END) AS TotalSavings " +
                    "FROM Products";

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                double totalInvestment = rs.getDouble("TotalInvestment");
                double totalSavings = rs.getDouble("TotalSavings");

                investmentField.setText(String.valueOf(totalInvestment));
                savingsField.setText(String.valueOf(totalSavings));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static double calculatePeriodSavings(String period, boolean isMonthly, Connection conn) throws SQLException {
        String query = "SELECT SUM(solarprice) AS PeriodSavings FROM Products WHERE productType = 'sale' AND ";
        if (isMonthly) {
            query += "transactionDate >= DATE_SUB(CURDATE(), INTERVAL ? MONTH)";
        } else {
            query += "transactionDate >= DATE_SUB(CURDATE(), INTERVAL ? YEAR)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            if (isMonthly) {
                switch (period) {
                    case "Last 3 months":
                        pstmt.setInt(1, 3);
                        break;
                    case "Last 6 months":
                        pstmt.setInt(1, 6);
                        break;
                    case "Last 9 months":
                        pstmt.setInt(1, 9);
                        break;
                    case "Last 12 months":
                        pstmt.setInt(1, 12);
                        break;
                }
            } else {
                switch (period) {
                    case "Last Year":
                        pstmt.setInt(1, 1);
                        break;
                    case "Last 2 Years":
                        pstmt.setInt(1, 2);
                        break;
                }
            }

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("PeriodSavings");
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Solar Panel ROI Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Solar Panel ROI Calculator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));

        JLabel investmentLabel = new JLabel("Total Investment (USD):");
        JLabel savingsLabel = new JLabel("Total Savings (USD):");

        JTextField investmentField = new JTextField(10);
        JTextField savingsField = new JTextField(10);

        investmentField.setEditable(false);
        savingsField.setEditable(false);

        JRadioButton monthlyButton = new JRadioButton("Monthly", true);
        JRadioButton yearlyButton = new JRadioButton("Yearly");
        ButtonGroup group = new ButtonGroup();
        group.add(monthlyButton);
        group.add(yearlyButton);

        JComboBox<String> periodComboBox = new JComboBox<>();
        periodComboBox.setEnabled(false);

        JButton calculateButton = new JButton("Calculate ROI");
        calculateButton.setBackground(new Color(0, 153, 0));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);

        JLabel resultLabel = new JLabel("ROI: ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        panel.add(investmentLabel);
        panel.add(investmentField);
        panel.add(savingsLabel);
        panel.add(savingsField);
        panel.add(monthlyButton);
        panel.add(yearlyButton);
        panel.add(new JLabel("Select Time Period:"));
        panel.add(periodComboBox);
        panel.add(new JLabel());
        panel.add(calculateButton);
        panel.add(new JLabel());
        panel.add(resultLabel);

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        populateFields(investmentField, savingsField);

        monthlyButton.addActionListener(e -> {
            periodComboBox.removeAllItems();
            periodComboBox.addItem("Last 3 months");
            periodComboBox.addItem("Last 6 months");
            periodComboBox.addItem("Last 9 months");
            periodComboBox.addItem("Last 12 months");
            periodComboBox.setEnabled(true);
        });

        yearlyButton.addActionListener(e -> {
            periodComboBox.removeAllItems();
            periodComboBox.addItem("Last Year");
            periodComboBox.addItem("Last 2 Years");
            periodComboBox.setEnabled(true);
        });

        calculateButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/roi", "root", "")) {
                double totalInvestment = Double.parseDouble(investmentField.getText());
                if (periodComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a time period.");
                    return;
                }

                String selectedPeriod = (String) periodComboBox.getSelectedItem();
                boolean isMonthly = monthlyButton.isSelected();

                double periodSavings = calculatePeriodSavings(selectedPeriod, isMonthly, conn);
                double roi = calculateROI(periodSavings, totalInvestment);

                resultLabel.setText("ROI: " + String.format("%.2f", roi) + "%");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}
