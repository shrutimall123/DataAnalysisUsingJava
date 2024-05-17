package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GroundImpactAnalysis extends JFrame {

    private static final String CSV_FILE = "javacac/src/main/java/com/example/Cleaned_Airplane2.csv";

    public GroundImpactAnalysis() {
        initUI();
    }

    private void initUI() {
        CategoryDataset dataset = createDataset();
        JFreeChart barChart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);

        pack();
        setTitle("Ground Impact Analysis");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<Integer, Double> groundFatalitiesMap = loadData();

        for (Map.Entry<Integer, Double> entry : groundFatalitiesMap.entrySet()) {
            dataset.addValue(entry.getValue(), "Ground Fatalities", entry.getKey());
        }

        return dataset;
    }

    private Map<Integer, Double> loadData() {
        Map<Integer, Double> groundFatalitiesMap = new HashMap<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);

                if (fields.length > 16) {
                    double groundFatalities = fields[15].isEmpty() ? 0 : Double.parseDouble(fields[15]);
                    int year = fields[0].isEmpty() ? 0 : Integer.parseInt(fields[0].split("/")[2]);

                    groundFatalitiesMap.put(year, groundFatalitiesMap.getOrDefault(year, 0.0) + groundFatalities);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return groundFatalitiesMap;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Ground Impact Analysis",
                "Year",
                "Number of Ground Fatalities",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GroundImpactAnalysis ex = new GroundImpactAnalysis();
            ex.setVisible(true);
        });
    }
}
