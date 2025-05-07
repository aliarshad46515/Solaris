package Charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import Solaris.DBConnector;

import javax.swing.*;
import java.awt.*;

public class PieChart extends JPanel {

    private final DBConnector db = new DBConnector();

    public PieChart() {
        super();
        setLayout(new BorderLayout());
        add(createChartPanel(), BorderLayout.CENTER);
    }

    public JPanel createPieChartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        panel.add(createChartPanel(), BorderLayout.CENTER);
        return panel;
    }

    public JPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlinePaint(null);
        return new ChartPanel(chart);
    }

    public JFreeChart createChart(DefaultPieDataset dataset) {
        return ChartFactory.createPieChart(
                "Products",
                dataset,
                true,
                true,
                false
        );
    }

    public DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        db.prodChart(dataset);
        return dataset;
    }
}