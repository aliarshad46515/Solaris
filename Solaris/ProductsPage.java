package Solaris;

import  java.awt.*;
import javax.swing.*;

class ProductsPage extends JFrame {
    void products() {
        setTitle("Products-Solaris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage("Icon.png");
        setIconImage(icon);
        setLayout(new BorderLayout());
        setSize(420, 420);
        JPanel navBar = new JPanel(new BorderLayout());
        JTextField searchBar = new JTextField(10);
//        searchBar.setPreferredSize(new Dimension(120, 20));
        JPanel searchPanel = new JPanel(new BorderLayout());
        navBar.add(searchPanel, BorderLayout.EAST);
        searchPanel.add(searchBar, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        ImageIcon search = new ImageIcon("search_icon.png");
        Image ico = search.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        JLabel searchIco = new JLabel(new ImageIcon(ico));
        searchPanel.add(searchIco, BorderLayout.EAST);

        setVisible(true);
    }

    public static void main(String[] args) {
        ProductsPage pg = new ProductsPage();
        pg.products();
    }
}
