package com.example;



import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TimeCategory extends JFrame {

    public TimeCategory(String title) {
        super(title);

        // Create Dataset
        DefaultCategoryDataset dataset = createDataset();
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Time Category Histogram", 
                "Time Category", 
                "Count", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);

        // Customize the chart
        chart.getCategoryPlot().setDomainGridlinesVisible(true);

        // Add chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // This map will store the count of each time category
        Map<String, Integer> timeCategoryCount = new HashMap<>();
        timeCategoryCount.put("Morning", 0);
        timeCategoryCount.put("Afternoon", 0);
        timeCategoryCount.put("Evening", 0);
        timeCategoryCount.put("Night", 0);
        timeCategoryCount.put("Late Night", 0);

        // Load data from CSV
        try (CSVParser parser = new CSVParser(new FileReader("javacac/src/main/java/com/example/Cleaned_Airplane2.csv"), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                String category = record.get("Time_Category");
                if (timeCategoryCount.containsKey(category)) {
                    timeCategoryCount.put(category, timeCategoryCount.get(category) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Perform statistical analysis
        performStatisticalAnalysis(timeCategoryCount);

        // Add data to the dataset
        for (Map.Entry<String, Integer> entry : timeCategoryCount.entrySet()) {
            dataset.addValue(entry.getValue(), "Time Categories", entry.getKey());
        }

        return dataset;
    }

    private void performStatisticalAnalysis(Map<String, Integer> timeCategoryCount) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer count : timeCategoryCount.values()) {
            stats.addValue(count);
        }

        double mean = stats.getMean();
        double median = stats.getPercentile(50);
        double stdDev = stats.getStandardDeviation();
        int mode = findMode(timeCategoryCount);

        System.out.println("Statistical Analysis:");
        System.out.println("Mean: " + mean);
        System.out.println("Median: " + median);
        System.out.println("Standard Deviation: " + stdDev);
        System.out.println("Mode: " + mode);
    }

    private int findMode(Map<String, Integer> timeCategoryCount) {
        int maxValue = 0;
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry1 : timeCategoryCount.entrySet()) {
            int count = 0;
            for (Map.Entry<String, Integer> entry2 : timeCategoryCount.entrySet()) {
                if (entry2.getValue().equals(entry1.getValue())) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = entry1.getValue();
            }
        }
        return maxValue;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimeCategoryAnalysis example = new TimeCategoryAnalysis("Time Category Histogram");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}

