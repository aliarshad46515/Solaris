package Charts;

import Solaris.DBConnector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

import java.awt.*;

public class LineChart extends JPanel {
    DBConnector db = new DBConnector();
    public LineChart() {
        super(new BorderLayout());
        add(createChartPanel(), BorderLayout.CENTER);
    }

    private DefaultCategoryDataset getDataSet() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        db.getUserRegistrationDataset(dataset);
        return dataset;
    }

    private JPanel createChartPanel() {
        JFreeChart chart = createChart(getDataSet());
        return new ChartPanel(chart);
    }

    public JPanel createLineChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        panel.add(createChartPanel(), BorderLayout.CENTER);
        return panel;
    }

    private static JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createLineChart(
                "User Registrations",
                "Month",
                "User Count",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
}
