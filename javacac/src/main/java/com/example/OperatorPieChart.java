package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OperatorPieChart {
    public static void main(String[] args) {
        // Read data from CSV file
        Map<String, Integer> operatorCounts = readDataFromCSV("javacac/src/main/java/com/example/Cleaned_Airplane1.csv");

        // Create dataset for the pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<String, Integer> entry : operatorCounts.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Frequency Distribution of Operators",
                dataset,
                true,
                true,
                false
        );

        // Display the pie chart in a JFrame
        JFrame frame = new JFrame("Operator Pie Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }

    private static Map<String, Integer> readDataFromCSV(String filePath) {
        Map<String, Integer> operatorCounts = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            // Skip the header row
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(",");
                String operator = line[0];
                operatorCounts.put(operator, operatorCounts.getOrDefault(operator, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return operatorCounts;
    }
}
