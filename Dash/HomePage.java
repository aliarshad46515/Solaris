package Aniqa;

import Products.ProductList;
import Solaris.*;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class HomePage implements Runnable {
    private static int currentIndex = 0;
    private static JList<String> list;
    private static JPopupMenu popupMenu;

    public static void main(String[] args) {
        Thread homePage = new Thread(new HomePage());
        homePage.start();
    }

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Main Frame
        JFrame frame = new JFrame("Homepage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mainFrame = new JPanel();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setBorder(BorderFactory.createEmptyBorder());
        mainFrame.setBackground(Color.WHITE);

        // Navbar Panel
        JPanel navbar = createNavBar(frame);
//----------------------------------------------------------------------------------------------------------
        JPanel contentPanel = homePanel(frame);

        // Add Panels to Frame
        mainFrame.add(navbar, BorderLayout.NORTH);

//----------------------------------------------------------------------------------------------------------
        // Panel with Carousel Images
        JPanel imagepanel = new JPanel(new CardLayout());
        JLabel carouselLabel = new JLabel();

        carouselLabel.setOpaque(true);
        carouselLabel.setBackground(Color.WHITE);

        String[] imagesPath = {
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\c1.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\c2.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\c3.png",
                "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\2.png"
        };

        String[] images = {
                "Apple.png",
                "Google.png",
                "search_icon.png",
                "RIU.png"
        };
        final int[] index = {0};

// Set the first image immediately when the frame becomes visible
        try {
            ImageIcon icon = new ImageIcon(images[index[0]]);
            Image scaledImage = icon.getImage().getScaledInstance(1200, 350, Image.SCALE_SMOOTH);
            carouselLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception ex) {
            System.out.println("Error loading initial image: " + ex.getMessage());
        }

// Timer to cycle through the images after the first one is set
        Timer timer = new Timer(2000, e -> {
            try {
                index[0] = (index[0] + 1) % images.length;
                ImageIcon icon = new ImageIcon(images[index[0]]);
                Image scaledImage = icon.getImage().getScaledInstance(1200, 350, Image.SCALE_SMOOTH);
                carouselLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception ex) {
                System.out.println("Error loading image: " + ex.getMessage());
            }
        });
        timer.start();

        imagepanel.add(carouselLabel);
//----------------------------------------------------------------------------
        // Create main container panel
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        // Panel 1
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));

        String[] imagePaths1 = {"images/image1.jpg", "images/image2.jpg", "images/image3.jpg", "images/image4.jpg", "images/image5.jpg", "images/image6.jpg", "images/image7.jpg", "images/image8.jpg"};
        String[] labels1 = {"Label 1", "Label 2", "Label 3", "Label 4", "Label 5", "Label 6", "Label 7", "Label 8"};

        updatePanel(panel1, imagePaths1, labels1);

        // Panel 2
        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));

        String[] imagePaths2 = {"images/image5.jpg", "images/image6.jpg", "images/image7.jpg", "images/image8.jpg"};
        String[] labels2 = {"Label 5", "Label 6", "Label 7", "Label 8"};

        updatePanel(panel2, imagePaths2, labels2);

        panel1.setBounds(0, 0, 1200, 850);
        panel2.setBounds(1200, 0, 1200, 850);

        JScrollPane scrollPane = new JScrollPane(panel1);
        scrollPane.setBounds(1200, 0, 1200, 850);

        JScrollPane scrollPane2 = new JScrollPane(panel2);
        scrollPane.setBounds(1200, 0, 1200, 850);

        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                currentIndex = (currentIndex + rotation + imagePaths1.length) % imagePaths1.length;
                updatePanel(panel1, imagePaths1, labels1);
                panel1.revalidate();
                panel1.repaint();
            }
        });

        // Add panels to container
        containerPanel.setPreferredSize(new Dimension(1200, 550));
        containerPanel.add(scrollPane);
        containerPanel.add(scrollPane2);

        // Timer to switch panels every 3 seconds
//        Timer panelSwitchTimer = new Timer(3000, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                switchPanels(panel1, panel2);
//            }
//        });
//        panelSwitchTimer.start();

//        frame.getContentPane().add(containerScrollPane);
//--------------------------------------------------------------------------
//        JPanel chooseSolar = choosePanel();
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        GridBagConstraints c = new GridBagConstraints();
//        frame.setLayout(new FlowLayout());

//        JScrollPane scrollPane = new JScrollPane(mainPanel);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        c.fill = GridBagConstraints.CENTER;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(contentPanel, c);

        c.weighty = 0.0;
        c.gridy = 2;
        mainPanel.add(imagepanel, c);

        c.weighty = 0.0;
        c.gridy = 3;

        mainPanel.add(containerPanel, c);

//        c.weighty = 0.0;
//        c.gridy = 4;
//
//        mainPanel.add(chooseSolar, c);

        mainPanel.setBorder(null);
        mainFrame.add(mainPanel, BorderLayout.CENTER);

        footer.createFooter(mainFrame);

        JScrollPane jScrollPane = new JScrollPane(mainFrame);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(jScrollPane, BorderLayout.CENTER);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setResizable(false);

        frame.setVisible(true);
    }

    public static JPanel createNavBar(JFrame parentFrame) {
        JPanel navbar = new JPanel();
        navbar.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        navbar.setBackground(Color.WHITE);

        // Load the image
        String path = "C:\\Users\\aniqa\\IdeaProjects\\acp pro\\src\\Screenshot 2024-12-14 132722";
        ImageIcon logoIcon = new ImageIcon("dropdown.png");
        if (logoIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Failed to load logo image. Ensure the file path is correct.");
            return null;
        }
        Image logoImg = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        // Convert Image to BufferedImage
        BufferedImage circularImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circularImage.createGraphics();

        // Enable Anti-Aliasing
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a circular clip
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 50, 50);
        g2.setClip(circle);

        // Draw the image inside the circle
        g2.drawImage(logoImg, 0, 0, null);
        g2.dispose();

        // Use the circular image in JLabel
        JLabel logo = new JLabel(new ImageIcon(circularImage));

        // Company Name
        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new BorderLayout());
        companyPanel.setOpaque(false);

        JLabel companyName = new JLabel("ACProject");
        companyName.setForeground(Color.BLUE);
        companyName.setFont(new Font("Georgia", Font.ITALIC, 20));
        companyName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel tagline = new JLabel("alyanss");
        tagline.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        tagline.setForeground(Color.yellow);
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        companyPanel.add(companyName, BorderLayout.NORTH);
        companyPanel.add(tagline, BorderLayout.SOUTH);

        // Search Bar
        JTextField searchBar = new JTextField("Search for Products, Brands and More");
        searchBar.setColumns(30);
        searchBar.setFont(new Font("Arial", Font.PLAIN, 14));
        searchBar.setForeground(Color.GRAY);
        searchBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        searchBar.setPreferredSize(new Dimension(300, 30));
        searchBar.setMargin(new Insets(5, 5, 5, 15));

        String defaultText = "Search for Products, Brands and More";

        searchBar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchBar.getText().equals(defaultText)) {
                    searchBar.setText("");
                    searchBar.setForeground(Color.BLACK); // Change text color for user input
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchBar.getText().trim().isEmpty()) {
                    searchBar.setText(defaultText);
                    searchBar.setForeground(Color.GRAY); // Reset text color to default
                }
            }
        });

        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    //Call ProductList
                }
            }
        });

        java.util.List<String> loginSignup = Arrays.asList("Profile", "Logout", "Exit");
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String option : loginSignup) {
            listModel.addElement(option);
        }

        list = new JList<>(listModel);
        list.setVisibleRowCount(3);
        list.setBorder(null);
        popupMenu = new JPopupMenu();
        popupMenu.add(new JScrollPane(list));
        popupMenu.setBorder(null);

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    popupMenu.setVisible(false);
                }
            }
        });

        // Login Icon
        JLabel homeLabel = new JLabel("Home");
        homeLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        ImageIcon homeIcon = new ImageIcon("images/home.png"); // Replace with your icon
        Image homeImg = homeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        homeLabel.setIcon(new ImageIcon(homeImg));

        // Cart Label with Icon
        JLabel cartLabel = new JLabel("Cart");
        cartLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        ImageIcon cartIcon = new ImageIcon("images/shopping-cart.png");
        Image cartImg = cartIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        cartLabel.setIcon(new ImageIcon(cartImg));

        JLabel sellerLabel = new JLabel("Products");
        sellerLabel.setFont(new Font("Comic Sans MS", Font.ITALIC, 14));
        ImageIcon sellerIcon = new ImageIcon("images/Solar.png");
        Image sellerImg = sellerIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        sellerLabel.setIcon(new ImageIcon(sellerImg));

        JLabel profileLabel = new JLabel("Profile");
        ImageIcon profileIcon = new ImageIcon("images/user.png");
        Image profileImg = profileIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        profileLabel.setIcon(new ImageIcon(profileImg));
        profileLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point location = profileLabel.getLocationOnScreen();
                popupMenu.show(null, location.x + 10, location.y + profileLabel.getHeight() + 10);
            }
        });

        navbar.add(logo);
        navbar.add(companyPanel);
        navbar.add(searchBar);
        navbar.add(Box.createHorizontalStrut(300));
        navbar.add(homeLabel);
        navbar.add(Box.createHorizontalStrut(25));
        navbar.add(cartLabel);
        navbar.add(Box.createHorizontalStrut(25));
        navbar.add(sellerLabel);
        navbar.add(Box.createHorizontalStrut(25));
        navbar.add(profileLabel);

        Thread homePage = new Thread(new HomePage());
        Thread productPage = new Thread(new ProductList());

        cartLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                productPage.start();
                System.out.println(Thread.currentThread().getName());
                parentFrame.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cartLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        sellerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(parentFrame, "Become a Seller Clicked");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sellerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                profileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                homePage.start();
                parentFrame.dispose();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                homeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });

        return navbar;
    }

    private static void updatePanel(JPanel panel, String[] imagePaths, String[] labels) {
        panel.removeAll();
        for (int i = 0; i < imagePaths.length; i++) {
            JPanel imageBox = new JPanel();
            imageBox.setLayout(new BorderLayout());
            imageBox.setPreferredSize(new Dimension(270, 250));
            imageBox.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

            JLabel textLabel = new JLabel(labels[(currentIndex + i) % labels.length], SwingConstants.CENTER);
            textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
            textLabel.setForeground(Color.WHITE);
            textLabel.setBackground(Color.gray);
            textLabel.setOpaque(true);
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            textLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JLabel imageLabel = new JLabel(new ImageIcon(imagePaths[(currentIndex + i) % imagePaths.length]));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            imageBox.add(textLabel, BorderLayout.NORTH);
            imageBox.add(imageLabel, BorderLayout.CENTER);
            panel.add(imageBox);
        }
    }


//    private static JPanel createContentPanel() {
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 40));
//        contentPanel.setBackground(Color.white);
//        contentPanel.setPreferredSize(new Dimension(1200, 100));
//        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20)); // top, left, bottom, right
//
//        //label3
//        JLabel label3 = new JLabel("");
//        JComboBox<String> comboBox1 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
//        contentPanel.add(createComponentPanel(label3, comboBox1));
//        comboBox1.setBorder(null);
//        comboBox1.setBackground(Color.WHITE);
//        comboBox1.setFocusable(false);
//        comboBox1.setUI(ProdCatalog.comboUI(comboBox1));
//
//        // Label 4
//        JLabel label4 = new JLabel("");
//        JComboBox<String> comboBox2 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
//        contentPanel.add(createComponentPanel(label4, comboBox2));
//        comboBox2.setBorder(null);
//        comboBox2.setBackground(Color.WHITE);
//        comboBox2.setUI(ProdCatalog.comboUI(comboBox2));
//        comboBox2.setFocusable(false);
//
//        // Label 5
//        JLabel label5 = new JLabel("");
//        JComboBox<String> comboBox3 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
//        contentPanel.add(createComponentPanel(label5, comboBox3));
//        comboBox3.setBorder(null);
//        comboBox3.setBackground(Color.WHITE);
//        comboBox3.setUI(ProdCatalog.comboUI(comboBox3));
//        comboBox3.setFocusable(false);
//
//
//        // Label 8
//        JLabel label8 = new JLabel("");
//        JComboBox<String> comboBox4 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
//        comboBox4.setBorder(null);
//        comboBox4.setBackground(Color.WHITE);
//        contentPanel.add(createComponentPanel(label8, comboBox4));
//        comboBox4.setUI(ProdCatalog.comboUI(comboBox4));
//        comboBox4.setFocusable(false);
//
//        // Label 9
//        JLabel label9 = new JLabel("");
//        JComboBox<String> comboBox5 = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
//        contentPanel.add(createComponentPanel(label9, comboBox5));
//        comboBox5.setBorder(null);
//        comboBox5.setBackground(Color.WHITE);
//        comboBox5.setUI(ProdCatalog.comboUI(comboBox5));
//        comboBox5.setFocusable(false);
//
//        return contentPanel;
//    }

    //------------------------------------------------------------
    private static JPanel createComponentPanel(JLabel label, JComboBox<String> comboBox) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboBox.addActionListener(e -> System.out.println(label.getText() + " selected: " + comboBox.getSelectedItem()));
        panel.add(label);
        panel.add(comboBox);
        panel.setBackground(Color.WHITE);
        return panel;
    }

    // Helper method to create a standalone label
    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(130, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.out.println(text + " clicked.");
            }
        });
        return label;
    }
    //---------------------------------------------------
    private static void switchPanels(JPanel panel1, JPanel panel2) {
        Timer animationTimer = new Timer(10, null);
        animationTimer.addActionListener(new ActionListener() {
            int x1 = panel1.getX();
            int x2 = 1300;

            @Override
            public void actionPerformed(ActionEvent e) {
                x1 -= 20; // Move current panel left
                x2 -= 20; // Move new panel left

                panel1.setBounds(x1, 0, 1300, 850);
                panel2.setBounds(x2, 0, 1300, 850);

                if (x1 <= -1300) { // Once the current panel is off-screen
                    panel1.setBounds(1300, 0, 1300, 850);
                    panel2.setBounds(0, 0, 1300, 850); // Position the new panel in view
                    animationTimer.stop();
                }
            }
        });
        animationTimer.start();
    }

    private static JPanel homePanel(JFrame parentFrame) {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        String storeName = "Solaris";

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        JLabel productImage = new JLabel(new ImageIcon(new ImageIcon("Solar.png").getImage().getScaledInstance(300, 350, Image.SCALE_SMOOTH)));
        productImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        imagePanel.add(productImage);
        mainPanel.add(imagePanel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel titleLabel = new JLabel("<html><font color='blue'>" + storeName + "</font><br><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Leading the Way in Solar Energy</b></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 90, 10, 10));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] features = {
                "Reduce your carbon footprint and help protect the environment.",
                "Reliable and renewable energy for long-term savings and sustainability.",
                "Eco-friendly systems designed for optimal efficiency and performance.",
                "Ongoing support and maintenance for your solar system.",
                "Clean, sustainable energy for a brighter future.",
                "Invest in a greener future for yourself and generations to come."
        };

        for (String feature : features) {
            JLabel featureLabel = new JLabel("✔ " + feature);
            featureLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            featureLabel.setForeground(new Color(0, 153, 0));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            featureLabel.setBorder(BorderFactory.createEmptyBorder(5, 90, 5, 10));
            contentPanel.add(featureLabel);
        }

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel descriptionLabel = new JLabel("<html>Heterojunction Technology (HJT) merges the advantages of crystalline<br>" +
                "and amorphous silicon thin-film technologies, delivering superior light<br>" +
                "absorption and effective surface passivation for optimal efficiency.</html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(new Color(80, 80, 80));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 90, 10, 10));
        contentPanel.add(descriptionLabel);

        JXPanel buttonPanel = new JXPanel(new FlowLayout());

        JXButton aboutButton = new JXButton("About Us");

        buttonPanel.add(aboutButton);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.setPreferredSize(new Dimension(1200, 400));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        return mainPanel;
    }

//    private static JPanel addPanel(String title, String description) {
//        JPanel panel = new JPanel(new BorderLayout(10, 10));
//        panel.setBackground(Color.WHITE);
//
//        JLabel titleLabel = new JLabel(title);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        panel.add(titleLabel, BorderLayout.NORTH);
//
//        JLabel descriptionLabel = new JLabel(description);
//        panel.add(descriptionLabel, BorderLayout.CENTER);
//
//        return panel;
//    }

//    private static JPanel choosePanel(){
//        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
//        panel.setBackground(Color.WHITE);
//
//        panel.add(addPanel("Fast Shipping", "Get your panels delivered quickly."));
//        panel.add(addPanel("Years of Experience", "Trust our expertise."));
//        panel.add(addPanel("Expert Support", "We're here to help."));
//        panel.add(addPanel("Customize Your System", "Tailor your solution."));
//        panel.add(addPanel("Guaranteed Quality", "TÜV & CE certified panels."));
//        panel.add(addPanel("Free Installation Guide", "Easy setup with our guide."));
//
//        return panel;
//    }
}