package Aniqa;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class user{
    public static void main(String[] args) {
        JFrame frame = new JFrame("Client Search");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Enter your search:");
        JTextField searchBar = new JTextField(20);
        JButton searchButton = new JButton("Search");

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(searchBar);
        panel.add(searchButton);

        frame.add(panel);
        frame.setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchBar.getText();
                if (!query.isEmpty()) {
                    try (Socket socket = new Socket("localhost", 5000);
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                        out.println(query);
                        JOptionPane.showMessageDialog(frame, "Search sent to server: " + query);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Search bar cannot be empty!");
                }
            }
        });
    }
}
