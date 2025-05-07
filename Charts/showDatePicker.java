package Charts;


import org.jdatepicker.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class showDatePicker {
    public static void showDatePickerDialog() {
        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

        int result = JOptionPane.showConfirmDialog(null, datePicker, "Select Date Range for ROI Calculation", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date selectedDate = (Date) datePicker.getModel().getValue();
            if (selectedDate != null) {
                JOptionPane.showMessageDialog(null, "Selected Date: " + selectedDate, "Date Selected", JOptionPane.INFORMATION_MESSAGE);
                // calculateRoiForDate(selectedDate);
            }
        }
    }
}