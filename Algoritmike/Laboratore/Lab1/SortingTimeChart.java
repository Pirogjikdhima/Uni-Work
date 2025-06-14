package Laboratore.Lab1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class SortingTimeChart extends JFrame {
    public SortingTimeChart() {
        // Dataset for the chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Sample data for MergeSort and QuickSort
        Object[][] data = {
                {"Iteration 1", 8200, 13100},
                {"Iteration 2", 41500, 27000},
                {"Iteration 3", 491500, 366700},
                {"Iteration 4", 1516500, 1739400},
                {"Iteration 5", 12889000, 23058200},
                {"Iteration 6", 102137300, 168834200}
        };

        // Adding data to the chart dataset
        for (Object[] row : data) {
            String iteration = (String) row[0];
            dataset.addValue((Number) row[1], "MergeSort", iteration);
            dataset.addValue((Number) row[2], "QuickSort", iteration);
        }

        // Create the line chart
        JFreeChart lineChart = ChartFactory.createLineChart(
                "MergeSort vs QuickSort Execution Time",
                "Iteration",
                "Time (ns)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Customizing the Y-axis to show exact values without scientific notation
        CategoryPlot plot = lineChart.getCategoryPlot();
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(new DecimalFormat("#,###"));

        // Creating the chart panel
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 400));

        // Creating the table with data
        String[] columnNames = {"Iteration", "MergeSort Time (ns)", "QuickSort Time (ns)"};
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(800, 150));

        // Adding the chart panel and table to the frame
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(chartPanel, BorderLayout.CENTER);
        contentPanel.add(tableScrollPane, BorderLayout.SOUTH);
        setContentPane(contentPanel);

        // Set frame properties
        setTitle("Sorting Algorithm Comparison");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingTimeChart example = new SortingTimeChart();
            example.setVisible(true);
        });
    }
}
