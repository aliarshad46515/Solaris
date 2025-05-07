package Products;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;

import Aniqa.HomePage;
import Aniqa.footer;
import Solaris.*;
import org.jdesktop.swingx.JXButton;


public class ProductList implements Runnable{
    private static boolean isSeached = false;
    private static boolean selectedSortLabel = false;
    private static ArrayList<JLabel> labelList = new ArrayList<>();
    private static JPanel productPanel = null;

    public static class RemoteEventSource {
        private static ArrayList<RemoteEventListener> listeners = new ArrayList<>();

        public static void addListener(RemoteEventListener listener) {
            listeners.add(listener);
        }

        public static void triggerEvent(EventObject e) {
            for (RemoteEventListener listener : listeners) {
                listener.handleRemoteEvent(e);
            }
        }
    }

    public interface RemoteEventListener {
        void handleRemoteEvent(EventObject e);
    }

    public static void main(String[] args) {
        Thread productPage = new Thread(new ProductList());
        productPage.start();
    }

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Solaris - Your Own Solar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);
        DBConnector db = new DBConnector();

        JPanel topPanel = HomePage.createNavBar(frame);

        JPanel filterPanel = createFilterPanel();

        JPanel sortingPanel = new JPanel();
        sortingPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        sortingPanel.setBackground(Color.WHITE);
        sortingPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel sortBy = new JLabel("Sort by");
        sortBy.setBorder(BorderFactory.createEmptyBorder());
        sortBy.setFont(new Font("Arial", Font.PLAIN, 16));
        sortingPanel.add(sortBy);

        String[] sortOptions = {"Price: Low to High", "Price: High to Low", "Name: A-Z", "Name: Z-A"};
        for (String option : sortOptions) {
            JLabel sortLabel = createSortLabel(option);
            sortingPanel.add(sortLabel);
            labelList.add(sortLabel);
        }

        productPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane productScrollPane = new JScrollPane(productPanel);
        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productScrollPane.setBorder(null);

//        searchIcon.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                productPanel.removeAll();
//                ArrayList<JPanel> prodCards =  db.searchProducts(searchBar.getText());
//                for (JPanel card : prodCards) {
//                    productPanel.add(card);
//                }
//                isSeached = true;
//                productScrollPane.revalidate();
//                productScrollPane.repaint();
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                searchIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//            }
//        });
//
//        searchBar.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
//                    productPanel.removeAll();
//                    ArrayList<JPanel> prodCards =  db.searchProducts(searchBar.getText());
//                    for (JPanel card : prodCards) {
//                        productPanel.add(card);
//                    }
//                    isSeached = true;
//                    productScrollPane.revalidate();
//                    productScrollPane.repaint();
//                }
//            }
//        });

        RemoteEventSource.addListener(e -> {
            Object src = e.getSource();
            if(src instanceof JCheckBox){
                System.out.println("Checkbox");
                JCheckBox srcCheck = (JCheckBox) src;
                productPanel.removeAll();
                ArrayList<JPanel> prodCards =  db.searchProducts(srcCheck.getText());
                for (JPanel card : prodCards) {
                    productPanel.add(card);
                }
                isSeached = true;
                productScrollPane.revalidate();
                productScrollPane.repaint();
            }

            if (src instanceof JSlider) {
                System.out.println("Slider");
                JSlider sliderCheck = (JSlider) src;
                productPanel.removeAll();
                ArrayList<JPanel> prodCards =  db.searchProdPrice(String.valueOf(sliderCheck.getValue()));
                for (JPanel card : prodCards) {
                    productPanel.add(card);
                }
                isSeached = true;
                productScrollPane.revalidate();
                productScrollPane.repaint();
            }

            if (src instanceof JComboBox<?>) {
                System.out.println("Slider");
                JComboBox<?> sliderCheck = (JComboBox<?>) src;
                productPanel.removeAll();
                ArrayList<JPanel> prodCards =  db.searchProdPrice(String.valueOf(sliderCheck.getSelectedItem()));
                for (JPanel card : prodCards) {
                    productPanel.add(card);
                }
                isSeached = true;
                productScrollPane.revalidate();
                productScrollPane.repaint();
            }
        });

        if(!isSeached) {
            ArrayList<JPanel> cards = db.getProductCard();

            for (JPanel card : cards) {
                productPanel.add(card);
            }
        }

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(sortingPanel, BorderLayout.NORTH);
        mainPanel.add(productScrollPane, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(filterPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setBackground(Color.GRAY);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static JLabel createSortLabel(String option) {
        JLabel sortLabel = new JLabel(option);
        sortLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sortLabel.setBackground(new Color(245, 245, 245));
        sortLabel.setForeground(Color.BLACK);
        sortLabel.setBorder(BorderFactory.createEmptyBorder());

        sortLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                sortLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                for (JLabel label : labelList) {
                    label.setForeground(Color.BLACK);
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    label.setBorder(BorderFactory.createEmptyBorder());
                }
                sortLabel.setForeground(new Color(19, 42, 194, 255));
                sortLabel.setFont(new Font("Arial", Font.BOLD, 14));
                sortLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(19, 42, 194, 255)));
                if (option.equals("Price: Low to High")) {
                    SortingMethods.sortProductsByPrice(true, productPanel);
                } else if (option.equals("Price: High to Low")) {
                    SortingMethods.sortProductsByPrice(false, productPanel);
                } else if (option.equals("Name: A-Z")) {
                    SortingMethods.sortProductsByName(Comparator.comparing(SortingMethods::extractName), productPanel);
                } else if (option.equals("Name: Z-A")) {
                    SortingMethods.sortProductsByName(Comparator.comparing(SortingMethods::extractName).reversed(), productPanel);
                }
                productPanel.revalidate();
                productPanel.repaint();
            }
        });

        return sortLabel;
    }

    private static JPanel createFilterPanel() {
        JPanel mainFilter = new JPanel(new BorderLayout(10, 10));
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setPreferredSize(new Dimension(250, 0));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel filterTitle = new JLabel("Filters");
        filterTitle.setFont(new Font("Arial", Font.BOLD, 16));
        filterPanel.add(filterTitle, gbc);

        gbc.gridy++;
        filterPanel.add(createPriceRangePanel(), gbc);

        gbc.gridy++;
        filterPanel.add(createCollapsibleSection("Categories", new String[]{"Mono-SI", "Poly-SI", "TFSC", "A-Si", "CVP & HCVP", "CdTe", "Biohybrid"}), gbc);

        gbc.gridy++;
        filterPanel.add(createCollapsibleSection("Brands", new String[]{"SunPower", "REC Solar", "Jinko Solar", "Panasonic", "LONGi", "JA Solar"}), gbc);

        filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));

        mainFilter.add(filterPanel, BorderLayout.CENTER);

        return mainFilter;
    }

    private static JPanel createCollapsibleSection(String title, String[] options) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);

        JLabel toggleLabel = new JLabel(title);
        toggleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        toggleLabel.setBackground(Color.WHITE);
        toggleLabel.setBorder(BorderFactory.createEmptyBorder());
        toggleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionPanel.add(toggleLabel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(5, 20, 5, 5));

        ButtonGroup group = new ButtonGroup();

        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkBox.setFont(new Font("Arial", Font.PLAIN, 14));
            checkBox.setBackground(Color.WHITE);
            group.add(checkBox);
            contentPanel.add(checkBox);
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RemoteEventSource.triggerEvent(e);
                }
            });
        }

        contentPanel.setVisible(true);

        toggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                contentPanel.setVisible(!contentPanel.isVisible());
                sectionPanel.revalidate();
                sectionPanel.repaint();
            }
        });

        sectionPanel.add(contentPanel);
        toggleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                toggleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        return sectionPanel;
    }

    private static JPanel createPriceRangePanel() {
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBackground(Color.WHITE);

        JLabel priceLabel = new JLabel("PRICE");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.add(priceLabel);

        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sliderPanel.setBackground(Color.WHITE);

        JSlider priceSlider = new JSlider(0, 10000, 2000);
        priceSlider.setMajorTickSpacing(2000);
        priceSlider.setPaintTicks(false);
        priceSlider.setPaintTrack(true);
        priceSlider.setBackground(Color.WHITE);
        priceSlider.setPreferredSize(new Dimension(200, 20));
        sliderPanel.add(priceSlider);

        sliderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.add(sliderPanel);
        pricePanel.add(Box.createVerticalStrut(5));

        JPanel dropdownPanel = new JPanel();
        dropdownPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dropdownPanel.setBackground(Color.WHITE);

        String[] priceOptions = {"0", "1000", "2000", "5000", "10000"};
        JComboBox<String> minDropdown = new JComboBox<>(priceOptions);
        minDropdown.setPreferredSize(new Dimension(80, 25));
        dropdownPanel.add(minDropdown);

        JLabel toLabel = new JLabel(" to ");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        toLabel.setForeground(Color.GRAY);
        dropdownPanel.add(toLabel);

        JComboBox<String> maxDropdown = new JComboBox<>(priceOptions);
        maxDropdown.setPreferredSize(new Dimension(80, 25));
        maxDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                RemoteEventSource.triggerEvent(e);
            }
        });
        dropdownPanel.add(maxDropdown);

        priceSlider.addChangeListener(e -> {
            maxDropdown.setSelectedItem(String.valueOf(priceSlider.getValue()));
            RemoteEventSource.triggerEvent(e);
        });

        dropdownPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.add(dropdownPanel);

        return pricePanel;
    }


    private static JXButton createMenuButton(String text, String iconPath, Color bgColor) {
        JXButton button = new JXButton(text);
//        if (iconPath != null) {
//            ImageIcon icon = new ImageIcon(
//                    new ImageIcon(iconPath).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
//            button.setIcon(icon);
//            button.setIconTextGap(5);
//        }
        button.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(new EmptyBorder(8, 12, 8, 12));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        return button;
    }

    public static JPanel createProductCard(String name, String price, String discount, String description, String manufacture, String output, ImageIcon icon) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(250, 350));

        JLabel productImage = new JLabel(new ImageIcon(
                icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH)));
        productImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        productImage.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel productName = new JLabel(name);
        productName.setFont(new Font("Arial", Font.BOLD, 14));
        productName.setAlignmentX(Component.CENTER_ALIGNMENT);
        productName.setBorder(new EmptyBorder(0, 5, 10, 5));

        JLabel productPrice = new JLabel(price);
        productPrice.setFont(new Font("Arial", Font.PLAIN, 12));
        productPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        productPrice.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel productDiscount = new JLabel(discount);
        productDiscount.setFont(new Font("Arial", Font.PLAIN, 12));
        productDiscount.setForeground(new Color(76, 175, 80));
        productDiscount.setAlignmentX(Component.CENTER_ALIGNMENT);
        productDiscount.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel manufacturer = new JLabel(manufacture);
        manufacturer.setFont(new Font("Arial", Font.PLAIN, 12));
        manufacturer.setForeground(new Color(76, 175, 80));
        manufacturer.setAlignmentX(Component.CENTER_ALIGNMENT);
        manufacturer.setBorder(new EmptyBorder(0, 0, 10, 0));

        JXButton viewProductButton = new JXButton("View Product");
        viewProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewProductButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProductDetailsDialog(name, price, discount, description, manufacture, output, icon);
            }
        });

        card.add(productImage);
        card.add(productName);
        card.add(manufacturer);
        card.add(productPrice);
        card.add(productDiscount);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(viewProductButton);

        return card;
    }

    private static void showProductDetailsDialog(String name, String price, String discount, String description, String manufacturer, String output, ImageIcon icon) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Product Details");
        dialog.setSize(400, 500);
        dialog.setModal(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(Color.WHITE);

        JLabel productImage = new JLabel(new ImageIcon(
                icon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH)));
        productImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productName = new JLabel(name);
        productName.setFont(new Font("Arial", Font.BOLD, 16));
        productName.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel prodManufacturer = new JLabel("By: " + manufacturer);
        productName.setFont(new Font("Arial", Font.PLAIN, 14));
        productName.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel prodOutput = new JLabel("Power: " + output + "KWh");
        productName.setFont(new Font("Arial", Font.PLAIN, 14));
        productName.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel productPrice = new JLabel(price);
        productPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        productPrice.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel productDiscount = new JLabel("Discount: " + discount);
        productDiscount.setFont(new Font("Arial", Font.PLAIN, 14));
        productDiscount.setForeground(new Color(76, 175, 80));
        productDiscount.setBorder(new EmptyBorder(5, 0, 10, 0));

        JLabel productDes = new JLabel(description);
        productDes.setFont(new Font("Arial", Font.PLAIN, 14));
        productDes.setBorder(new EmptyBorder(5, 0, 5, 0));

        JXButton addToCartButton = new JXButton("Add to Cart");
        JXButton buyNowButton = new JXButton("Buy Now");
        addToCartButton.setFont(new Font("Arial", Font.PLAIN, 12));
        buyNowButton.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(buyNowButton);

        mainPanel.add(productImage);
        mainPanel.add(productName);
        mainPanel.add(prodManufacturer);
        mainPanel.add(prodOutput);
        mainPanel.add(productPrice);
        mainPanel.add(productDiscount);
        mainPanel.add(productDes);
        mainPanel.add(buttonPanel);

        dialog.add(mainPanel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

}
