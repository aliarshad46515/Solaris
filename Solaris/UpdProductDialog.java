package Solaris;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class UpdProductDialog {
    private JDialog dialog;
    private JTextField titleField, priceField, quantityField, txtImage1, txtImage2, txtImage3;
    private JTextArea descriptionField;
    private JComboBox<String> categoryField, powerOutputField, manufacturerField;
    private DBConnector db;

    public UpdProductDialog() {
        db = new DBConnector();
    }

    void updateProduct(Product prod) {
        initializeUI("Update Product", prod);
    }

    private void initializeUI(String text, Product prod) {
        dialog = new JDialog((Frame) null, text, true);
        dialog.setSize(600, 450);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel leftPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        titleField = new JTextField(24);
        leftPanel.add(createFieldPanel("Product Title:", titleField, true));

        powerOutputField = new JComboBox<>(new String[]{"Select Output(kWh)", "10", "100", "150", "200"});
        leftPanel.add(createFieldPanel("Power Output:", powerOutputField, true));
        powerOutputField.setUI(comboUI(powerOutputField));
        powerOutputField.setFocusable(false);

        priceField = new JTextField(24);
        addNumericValidation(priceField);
        leftPanel.add(createFieldPanel("Product Price:", priceField, true));

        categoryField = new JComboBox<>(new String[]{"Select Category", "Mono-SI", "Poly-SI", "TFSC", "A-Si", "CVP & HCVP", "CdTe", "Biohybrid"});
        leftPanel.add(createFieldPanel("Category:", categoryField, true));
        categoryField.setUI(comboUI(categoryField));
        categoryField.setFocusable(false);

        quantityField = new JTextField(24);
        addNumericValidation(quantityField);
        leftPanel.add(createFieldPanel("Product Quantity:", quantityField, true));

        JPanel rightPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        descriptionField = new JTextArea(3, 15);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionField);
        rightPanel.add(createFieldPanel("Product Description:", descriptionScroll, false));

        manufacturerField = new JComboBox<>(new String[]{"Select Manufacturer", "SunPower", "REC Solar", "Jinko Solar", "Panasonic", "LONGi", "JA Solar"});
        rightPanel.add(createFieldPanel("Manufacturer:", manufacturerField, true));
        manufacturerField.setUI(comboUI(manufacturerField));
        manufacturerField.setFocusable(false);

        String[] images = prod.getImgURLs();

        txtImage1 = new JTextField(15);
        txtImage1.setEditable(false);
        txtImage1.setText(images[0]);
        rightPanel.add(createFileChooserPanel("Select 1st Image:", txtImage1));

        txtImage2 = new JTextField(15);
        txtImage2.setText(images[1]);
        txtImage2.setEditable(false);
        rightPanel.add(createFileChooserPanel("Select 2nd Image:", txtImage2));

        txtImage3 = new JTextField(15);
        txtImage3.setText(images[2]);
        txtImage3.setEditable(false);
        rightPanel.add(createFileChooserPanel("Select 3rd Image:", txtImage3));

        formPanel.add(leftPanel);
        formPanel.add(rightPanel);

        if(text.equals("Update Product")) {
            titleField.setText(prod.getProdName());
            powerOutputField.setSelectedItem(prod.getOutput());
            manufacturerField.setSelectedItem(prod.getManufacturer());
            descriptionField.setText(prod.getProdDesc());
            priceField.setText(String.valueOf(prod.getProdPrice()));
            quantityField.setText(String.valueOf(prod.getProdQuantity()));
            categoryField.setSelectedItem(prod.getProdCategory());
        }

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton(text);
        btnAdd.setPreferredSize(new Dimension(150, 30));
        btnAdd.addActionListener(e -> {
            if (dialog.getTitle().equals("Add Product")) {
                handleAddProduct();
            } else {
                handleUpdateProduct(prod);
            }
        });
        buttonPanel.add(btnAdd);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void handleAddProduct() {
        if (db.addProd(titleField.getText(), descriptionField.getText(), priceField.getText(),
                (String) categoryField.getSelectedItem(), quantityField.getText(),
                new String[]{txtImage1.getText(), txtImage2.getText(), txtImage3.getText()})) {
            JOptionPane.showMessageDialog(dialog, "Product Added Successfully", "Successful", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dialog, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdateProduct(Product prod) {
        if (db.updateProduct(prod.getProdID(),titleField.getText(), descriptionField.getText(), priceField.getText(),
                (String) categoryField.getSelectedItem(), quantityField.getText(), (String) manufacturerField.getSelectedItem(), (String) powerOutputField.getSelectedItem(),
                new String[]{txtImage1.getText(), txtImage2.getText(), txtImage3.getText()})) {
            JOptionPane.showMessageDialog(dialog, "Product Updated Successfully", "Successful", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dialog, "Failed to update product", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static ComboBoxUI comboUI(JComboBox<String> categoryField) {
        return new BasicComboBoxUI() {
            @Override
            protected ComboBoxEditor createEditor() {
                return new ComboBoxEditor() {
                    private final JTextField editor = new JTextField();
                    {
                        editor.setBorder(BorderFactory.createLineBorder(Color.black));
                    }
                    @Override
                    public Component getEditorComponent() {
                        return editor;
                    }

                    @Override
                    public void setItem(Object anObject) {

                    }

                    @Override
                    public Object getItem() {
                        return null;
                    }

                    @Override
                    public void selectAll() {

                    }

                    @Override
                    public void addActionListener(ActionListener l) {

                    }

                    @Override
                    public void removeActionListener(ActionListener l) {

                    }
                };
            }
            protected JButton createArrowButton() {
                JButton button = new JButton();
                button.setIcon(new ImageIcon("dropdown.png"));
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(comboBox.getBackground());
                return button;
            }
        };
    }


    private static JPanel createFileChooserPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        JButton button = new JButton("Choose File");
        button.setPreferredSize(new Dimension(90, 20));
        button.setBorder(BorderFactory.createEmptyBorder());
        textField.setBorder(BorderFactory.createEmptyBorder());
        textField.setPreferredSize(new Dimension(60, 20));
        button.addActionListener(e -> selectImage(textField));
        panel.add(label, BorderLayout.NORTH);
        JPanel subPanel = new JPanel(new FlowLayout());
        JPanel fieldsPanel = new JPanel(new BorderLayout(1, 1));
        subPanel.add(fieldsPanel);
        fieldsPanel.add(button, BorderLayout.WEST);
        fieldsPanel.add(textField, BorderLayout.CENTER);
        fieldsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.add(subPanel);
        return panel;
    }

    private static JPanel createFieldPanel(String labelText, JComponent field, boolean fullWidth) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.NORTH);
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(0, 25));
        }
        if (fullWidth && field instanceof JTextField) {
            JPanel wrapper = new JPanel(new FlowLayout());
            wrapper.add(field);
            panel.add(wrapper, BorderLayout.CENTER);
        } else if (field instanceof JComboBox<?>) {
            Dimension size = field.getPreferredSize();
            size.width = 265;
            field.setPreferredSize(size);
            field.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            field.setBackground(Color.WHITE);
            JPanel catPanel = new JPanel(new FlowLayout());
            catPanel.add(field);
            panel.add(catPanel, BorderLayout.CENTER);
        }
        else {
            panel.add(field, BorderLayout.CENTER);
        }
        return panel;
    }

    private static void selectImage(JTextField textField) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                textField.setText(selectedFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void addNumericValidation(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }

    public static void main(String[] args) {
//        new UpdProductDialog().updateProduct();
    }
}
