package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirplaneDataAnalysisJ extends JFrame {
    private DefaultCategoryDataset dataset;

    public AirplaneDataAnalysisJ() {
        super("Airplane Data Analysis");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataset = new DefaultCategoryDataset();

        try {
            // Read CSV file
            FileReader fileReader = new FileReader("javacac/src/main/java/com/example/Cleaned_Airplane2.csv");
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

            // Descriptive statistics for numerical columns
            Map<String, Double> numericalSummary = new HashMap<>();
            for (CSVRecord record : parser) {
                for (String header : parser.getHeaderMap().keySet()) {
                    String value = record.get(header);
                    if (isNumeric(value)) {
                        double numericValue = Double.parseDouble(value);
                        dataset.addValue(numericValue, header, "Value");
                        numericalSummary.put(header, numericalSummary.getOrDefault(header, 0.0) + numericValue);
                    }
                }
            }

            // Display basic statistics for numerical columns
            System.out.println("Descriptive Statistics:");
            for (Map.Entry<String, Double> entry : numericalSummary.entrySet()) {
                String header = entry.getKey();
                double sum = entry.getValue();
                int count = parser.getRecords().size();
                double mean = sum / count;
                System.out.println(header + ":");
                System.out.println("  Sum: " + sum);
                System.out.println("  Count: " + count);
                System.out.println("  Mean: " + mean);
                // Add to dataset
                dataset.addValue(sum, "Sum", header);
                dataset.addValue(count, "Count", header);
                dataset.addValue(mean, "Mean", header);
            }

            // Close CSV parser
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error parsing numeric value: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create and set up the panel
        JFreeChart chart = ChartFactory.createBarChart(
                "Descriptive Statistics",
                "Column",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirplaneDataAnalysisJ ex = new AirplaneDataAnalysisJ();
            ex.setVisible(true);
        });
    }
}
