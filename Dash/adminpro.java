package Aniqa;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class adminpro {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/serverdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Panel - Top Searches");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Admin Panel - Frequent Searches");
        headerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        headerPanel.add(headerLabel);

        // Table to display data

        String[] columns = {"Search Query", "Total Count"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);

                // Alternate row color
                if (row % 2 == 0) {
                    component.setBackground(new Color(245, 245, 245));
                } else {
                    component.setBackground(Color.WHITE);
                }
                // Center align text and set font style
                if (component instanceof JLabel) {
                    ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
                    ((JLabel) component).setVerticalAlignment(SwingConstants.CENTER);
                    component.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
                }
                return component;
            }
        };

        // Table styling
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(0, 123, 255)); // Border color
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.setFont(new Font("Arial", Font.BOLD, 14));

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(400); // Wider for "Search Query"
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Narrower for "Count"

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));


        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton fetchButton = new JButton("Fetch Top Searches");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchButton.setBackground(new Color(0, 123, 255));
        fetchButton.setForeground(Color.WHITE);
        buttonPanel.add(fetchButton);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                    String query = "SELECT query, SUM(count) AS total_count " +
                            "FROM searches " +
                            "GROUP BY query " +
                            "HAVING total_count >1 " +
                            "ORDER BY total_count DESC";

                    try (Statement stmt = connection.createStatement();
                         ResultSet rs = stmt.executeQuery(query)) {

                        // Clear existing data
                        tableModel.setRowCount(0);

                        // Add data to the table
                        while (rs.next()) {
                            String searchQuery = rs.getString("query");
                            int totalCount = rs.getInt("total_count");
                            tableModel.addRow(new Object[]{searchQuery, totalCount});
                        }

                        JOptionPane.showMessageDialog(frame, "Data fetched successfully.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error fetching data.");
                }
            }
        });
    }
}
