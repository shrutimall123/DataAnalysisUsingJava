package com.example;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirplaneDataVisualizations extends ApplicationFrame {

    private static final String FILE_PATH = "javacac/src/main/java/com/example/Cleaned_Airplane1.csv";

    public AirplaneDataVisualizations(String title) {
        super(title);

        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new GridLayout(3, 1));

        chartPanel.add(createCasualtiesByYearChart());
        chartPanel.add(createCasualtiesByMonthChart());
        chartPanel.add(createCasualtiesByDayChart());

        this.add(chartPanel);
    }

    private ChartPanel createCasualtiesByYearChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> casualtiesByYear = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 18) { // Ensure the array has enough elements
                    String year = parts[17];
                    double fatalities = parseDouble(parts[12]);
                    casualtiesByYear.put(year, casualtiesByYear.getOrDefault(year, 0) + (int) fatalities);
                } else {
                    System.err.println("Invalid data format for line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : casualtiesByYear.entrySet()) {
            dataset.addValue(entry.getValue(), "Casualties", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart("Casualties by Year", "Year", "Number of Casualties", dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
    }

    private ChartPanel createCasualtiesByMonthChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> casualtiesByMonth = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 17) { // Ensure the array has enough elements
                    String month = parts[16];
                    double fatalities = parseDouble(parts[12]);
                    casualtiesByMonth.put(month, casualtiesByMonth.getOrDefault(month, 0) + (int) fatalities);
                } else {
                    System.err.println("Invalid data format for line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        for (int month = 1; month <= 12; month++) {
            String monthStr = String.format("%02d", month); // Format month to have leading zeros
            dataset.addValue(casualtiesByMonth.getOrDefault(monthStr, 0), "Casualties", String.valueOf(month));
        }

        JFreeChart chart = ChartFactory.createBarChart("Casualties by Month", "Month", "Number of Casualties", dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
    }

    private ChartPanel createCasualtiesByDayChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> casualtiesByDay = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 18) { // Ensure the array has enough elements
                    String day = parts[17];
                    double fatalities = parseDouble(parts[12]);
                    casualtiesByDay.put(day, casualtiesByDay.getOrDefault(day, 0) + (int) fatalities);
                } else {
                    System.err.println("Invalid data format for line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        for (int day = 1; day <= 31; day++) {
            String dayStr = String.format("%02d", day); // Format day to have leading zeros
            dataset.addValue(casualtiesByDay.getOrDefault(dayStr, 0), "Casualties", String.valueOf(day));
        }

        JFreeChart chart = ChartFactory.createBarChart("Casualties by Day", "Day", "Number of Casualties", dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
    }

    public static void main(String[] args) {
        AirplaneDataVisualizations chart = new AirplaneDataVisualizations("Airplane Data Visualizations");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0; // Or handle the error appropriately
        }
    }
}
