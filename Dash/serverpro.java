package Aniqa;

import java.io.*;
import java.net.*;
import java.sql.*;

public class serverpro{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/serverdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000);
             Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            System.out.println("Server is running and waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket, connection)).start();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, Connection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String searchQuery = in.readLine();
            if (searchQuery != null) {
                storeSearchQuery(searchQuery, connection);
                System.out.println("Search query received: " + searchQuery);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void storeSearchQuery(String query, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO searches (query, count) VALUES (?, 1) ON DUPLICATE KEY UPDATE count = count + 1")) {
            statement.setString(1, query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
