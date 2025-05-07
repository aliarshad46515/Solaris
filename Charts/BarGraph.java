package Charts;

import Solaris.DBConnector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class BarGraph extends JPanel {
    static DBConnector db = new DBConnector();

    public BarGraph() {
        super();
        setLayout(new BorderLayout());
        add(createChartPanel(), BorderLayout.CENTER);
    }

    public JPanel createBarGraphPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        panel.add(createChartPanel(), BorderLayout.CENTER);
        return panel;
    }

    public static JPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }

    private static JFreeChart createChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Monthly Sales",
                "Month",
                "Sales",
                dataset,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        db.getDatasetFromDatabase(dataset);
        return dataset;
    }
}
