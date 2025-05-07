package Solaris;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class ProdCatalog {
    public static void addProduct() {
        DBConnector db = new DBConnector();
        JDialog dialog = new JDialog((Frame) null, "Add Product", true);
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel leftPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JTextField titleField = new JTextField(24);
        leftPanel.add(createFieldPanel("Product Title:", titleField, true));

        JComboBox<String> powerOutputField = new JComboBox<>(new String[]{"Select Output(kWh)", "10", "100", "150", "200"});
        leftPanel.add(createFieldPanel("Power Output:", powerOutputField, true));
        powerOutputField.setUI(comboUI(powerOutputField));
        powerOutputField.setFocusable(false);

        JTextField priceField = new JTextField(24);
        addNumericValidation(priceField);
        leftPanel.add(createFieldPanel("Product Price:", priceField, true));

        JComboBox<String> categoryField = new JComboBox<>(new String[]{"Select Category", "Mono-SI", "Poly-SI", "TFSC", "A-Si", "CVP & HCVP", "CdTe", "Biohybrid"});
        leftPanel.add(createFieldPanel("Category:", categoryField, true));
        categoryField.setUI(comboUI(categoryField));
        categoryField.setFocusable(false);

        JTextField quantityField = new JTextField(24);
        addNumericValidation(quantityField);
        leftPanel.add(createFieldPanel("Product Quantity:", quantityField, true));

        JPanel rightPanel = new JPanel(new GridLayout(5, 1, 10, 10));

        JTextArea descriptionField = new JTextArea(3, 15);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionField);
        rightPanel.add(createFieldPanel("Product Description:", descriptionScroll, false));

        JComboBox<String> manufacturerField = new JComboBox<>(new String[]{"Select Manufacturer", "SunPower", "REC Solar", "Jinko Solar", "Panasonic", "LONGi", "JA Solar"});
        rightPanel.add(createFieldPanel("Manufacturer:", manufacturerField, true));
        manufacturerField.setUI(comboUI(manufacturerField));
        manufacturerField.setFocusable(false);

        JTextField txtImage1 = new JTextField(15);
        txtImage1.setEditable(false);
        rightPanel.add(createFileChooserPanel("Select 1st Image:", txtImage1));

        JTextField txtImage2 = new JTextField(15);
        txtImage2.setEditable(false);
        rightPanel.add(createFileChooserPanel("Select 2nd Image:", txtImage2));

        JTextField txtImage3 = new JTextField(15);
        txtImage3.setEditable(false);
        rightPanel.add(createFileChooserPanel("Select 3rd Image:", txtImage3));

        formPanel.add(leftPanel);
        formPanel.add(rightPanel);

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Add Product");
        btnAdd.setPreferredSize(new Dimension(120, 30));
        btnAdd.addActionListener(e -> {
            if(db.addProd(titleField.getText(), descriptionField.getText(), priceField.getText(), (String) categoryField.getSelectedItem(), quantityField.getText(), new String[]{txtImage1.getText(), txtImage2.getText(), txtImage3.getText()})) {
                JOptionPane.showMessageDialog(dialog, "Product Added Successfully", "Successful", JOptionPane.PLAIN_MESSAGE);
                TablePanel.refreshTablePanel();
            } else if(titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || priceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(dialog, "Failed to add product", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(btnAdd);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
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

    public static void updatePricing() {
        JDialog dialog = new JDialog((Frame) null, "Set Product Prices", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField price10Field = new JTextField(15);
        addNumericValidation(price10Field);
        formPanel.add(new JLabel("Price for 10 kWh Output:"));
        formPanel.add(price10Field);

        JTextField price100Field = new JTextField(15);
        addNumericValidation(price100Field);
        formPanel.add(new JLabel("Price for 100 kWh Output:"));
        formPanel.add(price100Field);

        JTextField price150Field = new JTextField(15);
        addNumericValidation(price150Field);
        formPanel.add(new JLabel("Price for 150 kWh Output:"));
        formPanel.add(price150Field);

        JTextField price200Field = new JTextField(15);
        addNumericValidation(price200Field);
        formPanel.add(new JLabel("Price for 200 kWh Output:"));
        formPanel.add(price200Field);

        JPanel buttonPanel = new JPanel();
        JButton btnSave = new JButton("Save Prices");
        btnSave.setPreferredSize(new Dimension(120, 30));

        btnSave.addActionListener(e -> {
            if (price10Field.getText().isEmpty() || price100Field.getText().isEmpty() ||
                    price150Field.getText().isEmpty() || price200Field.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all price fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price10 = Double.parseDouble(price10Field.getText());
            double price100 = Double.parseDouble(price100Field.getText());
            double price150 = Double.parseDouble(price150Field.getText());
            double price200 = Double.parseDouble(price200Field.getText());

            boolean success = false;
//            savePricesToDB(price10, price100, price150, price200);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Prices saved successfully.", "Success", JOptionPane.PLAIN_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save prices.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(btnSave);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
