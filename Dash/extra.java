package Aniqa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class extra {
    public static void main(String[] args) {
        JFrame frame = new JFrame("UI Components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1230, 700); // Adjusted size to allow vertical layout
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        String[] labels = {"Web Compo", "Desktop Compo", "Mobile Compo",
                "Reporting", "Testing"};
        String[] defaultTexts = {
                "Default text for Web Components. This is a sample paragraph about Web Components.",
                "Default text for Desktop Components. This is a sample paragraph about Desktop Components.",
                "Default text for Mobile Components. This is a sample paragraph about Mobile Components.",
                "Default text for Reporting & Dashboards. This is a sample paragraph about Reporting & Dashboards.",
                "Default text for Testing & Debugging. This is a sample paragraph about Testing & Debugging."
        };
        String[] logoPaths = {
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\Screenshot 2024-12-14 132722.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\as.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\Screenshot 2024-12-14 132722.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\as.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\Screenshot 2024-12-14 132722.png"
        };

        for (int i = 0; i < 5; i++) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(200, 250));
            box.setBackground(new Color(200, 220, 240));
            box.setLayout(new BorderLayout());
            box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

            JLabel imageLabel = new JLabel();
            ImageIcon logo = new ImageIcon(logoPaths[i]);
            Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel textLabel = new JLabel(labels[i], JLabel.CENTER);
            textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
            textLabel.setForeground(Color.DARK_GRAY);
            textLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // Minimized gap
            textLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    textLabel.setForeground(Color.ORANGE); // Label color on hover
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    textLabel.setForeground(Color.DARK_GRAY);
                }
            });

            JLabel defaultTextLabel = new JLabel("<html><p style='width:180px;text-align:center;'>" + defaultTexts[i] + "</p></html>", JLabel.CENTER);
            defaultTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            defaultTextLabel.setForeground(Color.BLACK);
            defaultTextLabel.setBackground(new Color(230, 230, 250));
            defaultTextLabel.setOpaque(true);
            defaultTextLabel.setVisible(false); // Initially hidden
            defaultTextLabel.setPreferredSize(new Dimension(180, 150)); // Increased height

            box.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    defaultTextLabel.setVisible(true);
                    new Thread(() -> {
                        for (int y = 250; y >= 90; y -= 10) { // Adjusted height for more centered look
                            defaultTextLabel.setBounds(10, y, 180, 150); // Adjusted size and position
                            try {
                                Thread.sleep(10); // Smooth slide-up effect
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }).start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    defaultTextLabel.setVisible(false); // Hide on exit
                }
            });

            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.add(imageLabel, BorderLayout.CENTER);
            textPanel.add(textLabel, BorderLayout.SOUTH);

            box.add(textPanel, BorderLayout.NORTH);
            box.add(defaultTextLabel, BorderLayout.CENTER);

            frame.add(box);
        }

        frame.setVisible(true);
    }
}
