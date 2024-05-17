package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class YearAndDayHistogram {
    public static void main(String[] args) {
        // Path to your CSV file
        String csvFile = "javacac/src/main/java/com/example/Cleaned_Airplane1.csv";
        // Arrays to store month, year, and day counts
        int[] monthCounts = new int[12];
        int[] yearCounts = new int[100]; // Assuming years from 1923 to 2023
        int[] dayCounts = new int[31]; // Assuming days from 1 to 31

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Skip the header if any
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Assuming CSV is comma-separated
                if (data.length > 0) {
                    // Assuming the first column contains a date in the format "MM/DD/YYYY"
                    LocalDate date = LocalDate.parse(data[0], DateTimeFormatter.ofPattern("M/d/yyyy"));
                    int month = date.getMonthValue();
                    int year = date.getYear() - 1923; // Assuming years from 1923 to 2023
                    int day = date.getDayOfMonth();

                    // Increment count for corresponding month, year, and day
                    if (month >= 1 && month <= 12) {
                        monthCounts[month - 1]++;
                    }
                    if (year >= 0 && year < 100) {
                        yearCounts[year]++;
                    }
                    if (day >= 1 && day <= 31) {
                        dayCounts[day - 1]++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create dataset for month histogram
        DefaultCategoryDataset monthDataset = new DefaultCategoryDataset();
        for (int i = 0; i < 12; i++) {
            monthDataset.addValue(monthCounts[i], "Frequency", String.valueOf(i + 1));
        }

        // Create dataset for year histogram
        DefaultCategoryDataset yearDataset = new DefaultCategoryDataset();
        for (int i = 0; i < 100; i++) {
            yearDataset.addValue(yearCounts[i], "Frequency", String.valueOf(i + 1923));
        }

        // Create dataset for day histogram
        DefaultCategoryDataset dayDataset = new DefaultCategoryDataset();
        for (int i = 0; i < 31; i++) {
            dayDataset.addValue(dayCounts[i], "Frequency", String.valueOf(i + 1));
        }

        // Create charts
        JFreeChart monthChart = ChartFactory.createBarChart(
            "Month Histogram",
            "Month",
            "Frequency",
            monthDataset,
            PlotOrientation.VERTICAL,
            true, // Include legend
            true, // Include tooltips
            false // No URLs
        );

        JFreeChart yearChart = ChartFactory.createBarChart(
            "Year Histogram",
            "Year",
            "Frequency",
            yearDataset,
            PlotOrientation.VERTICAL,
            true, // Include legend
            true, // Include tooltips
            false // No URLs
        );

        JFreeChart dayChart = ChartFactory.createBarChart(
            "Day Histogram",
            "Day",
            "Frequency",
            dayDataset,
            PlotOrientation.VERTICAL,
            true, // Include legend
            true, // Include tooltips
            false // No URLs
        );

        // Display charts
        JFrame frame = new JFrame("Histograms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Month", new ChartPanel(monthChart));
        tabbedPane.addTab("Year", new ChartPanel(yearChart));
        tabbedPane.addTab("Day", new ChartPanel(dayChart));
        frame.setContentPane(tabbedPane);
        frame.pack();
        frame.setVisible(true);
    }
}