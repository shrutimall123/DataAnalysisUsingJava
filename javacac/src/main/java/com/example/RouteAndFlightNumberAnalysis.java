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
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RouteAndFlightNumberAnalysis extends JFrame {

    private static final String CSV_FILE = "javacac/src/main/java/com/example/Cleaned_Airplane2.csv";

    public RouteAndFlightNumberAnalysis() {
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel chartsPanel = new JPanel(new GridLayout(2, 1));

        CategoryDataset routeDataset = createRouteDataset();
        JFreeChart routeChart = createChart(routeDataset, "Top 20 Routes with Higher Accident Rates", "Route", "Number of Accidents");
        ChartPanel routeChartPanel = new ChartPanel(routeChart);
        chartsPanel.add(routeChartPanel);

        CategoryDataset flightNumberDataset = createFlightNumberDataset();
        JFreeChart flightNumberChart = createChart(flightNumberDataset, "Top 20 Flight Numbers with Higher Accident Rates", "Flight Number", "Number of Accidents");
        ChartPanel flightNumberChartPanel = new ChartPanel(flightNumberChart);
        chartsPanel.add(flightNumberChartPanel);

        panel.add(chartsPanel, BorderLayout.CENTER);

        String highestAccidentRoute = getHighestAccidentRoute();
        String highestAccidentFlightNumber = getHighestAccidentFlightNumber();
        JLabel highestAccidentLabel = new JLabel("<html>Flight with the highest accident rate: " + highestAccidentFlightNumber + "<br>" +
                "Route with the highest accident rate: " + highestAccidentRoute + "</html>", JLabel.CENTER);
        highestAccidentLabel.setFont(new Font("Serif", Font.BOLD, 16));
        panel.add(highestAccidentLabel, BorderLayout.NORTH);

        add(panel);

        setTitle("Route and Flight Number Analysis");
        setSize(800, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createRouteDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> routeAccidentMap = loadRouteData();

        // Get the top 20 routes
        List<Map.Entry<String, Integer>> topRoutes = routeAccidentMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : topRoutes) {
            dataset.addValue(entry.getValue(), "Accidents", entry.getKey());
        }

        return dataset;
    }

    private CategoryDataset createFlightNumberDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> flightNumberAccidentMap = loadFlightNumberData();

        // Get the top 20 flight numbers
        List<Map.Entry<String, Integer>> topFlightNumbers = flightNumberAccidentMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .collect(Collectors.toList());

        for (Map.Entry<String, Integer> entry : topFlightNumbers) {
            dataset.addValue(entry.getValue(), "Accidents", entry.getKey());
        }

        return dataset;
    }

    private Map<String, Integer> loadRouteData() {
        Map<String, Integer> routeAccidentMap = new HashMap<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);

                if (fields.length > 6) {
                    String route = fields[5];

                    routeAccidentMap.put(route, routeAccidentMap.getOrDefault(route, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routeAccidentMap;
    }

    private Map<String, Integer> loadFlightNumberData() {
        Map<String, Integer> flightNumberAccidentMap = new HashMap<>();
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(csvSplitBy);

                if (fields.length > 4) {
                    String flightNumber = fields[4];

                    flightNumberAccidentMap.put(flightNumber, flightNumberAccidentMap.getOrDefault(flightNumber, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flightNumberAccidentMap;
    }

    private String getHighestAccidentRoute() {
        Map<String, Integer> routeAccidentMap = loadRouteData();
        return routeAccidentMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " accidents)")
                .orElse("No data available");
    }

    private String getHighestAccidentFlightNumber() {
        Map<String, Integer> flightNumberAccidentMap = loadFlightNumberData();
        return flightNumberAccidentMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " accidents)")
                .orElse("No data available");
    }

    private JFreeChart createChart(CategoryDataset dataset, String title, String categoryAxisLabel, String valueAxisLabel) {
        return ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                valueAxisLabel,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            RouteAndFlightNumberAnalysis ex = new RouteAndFlightNumberAnalysis();
            ex.setVisible(true);
        });
    }
}
