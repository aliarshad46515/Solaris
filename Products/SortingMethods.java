package Products;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SortingMethods {
    public static void sortProductsByPrice(boolean ascending, JPanel productPanel) {
        ArrayList<JPanel> productItems = new ArrayList<>();
        for (Component comp : productPanel.getComponents()) {
            if (comp instanceof JPanel) {
                productItems.add((JPanel) comp);
            }
        }

        Collections.sort(productItems, new Comparator<JPanel>() {
            @Override
            public int compare(JPanel panel1, JPanel panel2) {
                double price1 = extractPrice(panel1);
                double price2 = extractPrice(panel2);

                if (ascending) {
                    return Double.compare(price1, price2);
                } else {
                    return Double.compare(price2, price1);
                }
            }
        });

        productPanel.removeAll();
        for (JPanel item : productItems) {
            productPanel.add(item);
        }
    }

    public static void sortProductsByName(Comparator<JPanel> comparator, JPanel productPanel) {
        ArrayList<JPanel> productItems = new ArrayList<>();
        for (Component comp : productPanel.getComponents()) {
            if (comp instanceof JPanel) {
                productItems.add((JPanel) comp);
            }
        }

        Collections.sort(productItems, comparator);

        productPanel.removeAll();
        for (JPanel item : productItems) {
            productPanel.add(item);
        }
    }

    public static double extractPrice(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                String text = ((JLabel) comp).getText();
                try{
                    if (text.contains("Rs.")) {
                        try {
                            System.out.println("Hello");
                            return Double.parseDouble(text.substring(3));
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing price: " + text);
                            return 0.0;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return 0.0;
    }

    public static String extractName(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JLabel) {
                String text = ((JLabel) comp).getText();
                try{
                    if (!text.contains("Rs.")) {
                        try {
                            System.out.println("Hello");
                            return text;
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing name: " + text);
                            return "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
