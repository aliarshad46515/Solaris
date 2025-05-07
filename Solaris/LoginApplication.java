package Solaris;

import Dash.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

class LoginApplication extends JFrame {
    DBConnector db = new DBConnector();

    void windowScreen() {
        setTitle("Login-Solaris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(800, 500);
        setBackground(Color.white);
        setLocationRelativeTo(null);
        setResizable(false);
        setFocusable(true);

        JPanel leftPanel = new JPanel(new CardLayout());
        JPanel rightPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        leftPanel.setPreferredSize(new Dimension(350, 400));
        rightPanel.setPreferredSize(new Dimension(350, 400));

        JLabel heading = new JLabel("Log in with an account");
        heading.setFont(new Font("MV Boli", Font.BOLD, 16));

        JPanel accLogos = new JPanel(new GridLayout(1, 4, 10, 10));
        String[] logos = new String[]{"Apple.png", "GitHub.png", "Google.png", "Microsoft.png"};
        for (String logo : logos) {
            JLabel logoLabel = new JLabel(new ImageIcon(logo));
            logoLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    logoLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    logoLabel.setBorder(null);
                }
            });
            accLogos.add(logoLabel);
        }

        JLabel heading2 = new JLabel("Or log in with email");
        heading2.setFont(new Font("MV Boli", Font.PLAIN, 14));

        JTextField userName = new JTextField("Username", 10);
        userName.setForeground(Color.GRAY);
        userName.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (userName.getText().equals("Username")) {
                    userName.setText("");
                    userName.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (userName.getText().isEmpty()) {
                    userName.setText("Username");
                    userName.setForeground(Color.GRAY);
                }
            }
        });

        JPasswordField passField = new JPasswordField("Password", 10);
        passField.setForeground(Color.GRAY);
        passField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(passField.getPassword()).equals("Password")) {
                    passField.setText("");
                    passField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (new String(passField.getPassword()).isEmpty()) {
                    passField.setText("Password");
                    passField.setForeground(Color.GRAY);
                }
            }
        });

        userName.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        passField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        JLabel forgetPass = new JLabel("Forgot your password?", SwingConstants.CENTER);
        forgetPass.setFont(new Font("MV Boli", Font.PLAIN, 14));

        JButton login = new JButton("Log in");
        login.setFont(new Font("MV Boli", Font.BOLD, 16));
        login.setEnabled(false);

        KeyAdapter enableLogin = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String userText = userName.getText();
                String passText = new String(passField.getPassword());
                login.setEnabled(!userText.equals("Username") && !userText.isEmpty() && !passText.equals("Password") && !passText.isEmpty());
            }
        };
        userName.addKeyListener(enableLogin);
        passField.addKeyListener(enableLogin);

        login.addActionListener(e -> {
            String username = userName.getText();
            String password = new String(passField.getPassword());
            if (db.validateLogin(username, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                db.insertLogEntry(username);
//                HomePage.homePage();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.add(passField);

        rightPanel.add(heading);
        rightPanel.add(accLogos);
        rightPanel.add(heading2);
        rightPanel.add(userName);
        rightPanel.add(passPanel);
        rightPanel.add(forgetPass);
        rightPanel.add(login);

        ImageIcon imgIco = new ImageIcon("Login.png");
        Image img = imgIco.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        JLabel logoImg = new JLabel(new ImageIcon(img));
        leftPanel.add(logoImg);
//        leftPanel.setBackground(new Color(192,192,192));

        add(leftPanel);
//        add(new JSeparator());
        add(rightPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
            new LoginApplication().windowScreen();
    }
}
