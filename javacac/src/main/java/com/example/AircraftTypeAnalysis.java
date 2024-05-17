package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;


import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AircraftTypeAnalysis extends ApplicationFrame {

    public AircraftTypeAnalysis(String title) {
        super(title);
        setContentPane(createPanel());
    }

    private ChartPanel createPanel() {
        DefaultCategoryDataset barDataset = createBarDataset();
        DefaultPieDataset pieDataset = createPieDataset();

        JFreeChart barChart = createBarChart(barDataset);
        JFreeChart pieChart = createPieChart(pieDataset);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        ApplicationFrame pieFrame = new ApplicationFrame("Aircraft Type Proportion");
        ChartPanel pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setPreferredSize(new Dimension(800, 600));
        pieFrame.setContentPane(pieChartPanel);
        pieFrame.pack();
        RefineryUtilities.centerFrameOnScreen(pieFrame);
        pieFrame.setVisible(true);

        return chartPanel;
    }

    private DefaultCategoryDataset createBarDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> aircraftTypeCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("javacac/src/main/java/com/example/Cleaned_Airplane2.csv"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String aircraftType = fields[6]; // Assuming aircraft type is the 7th column

                aircraftTypeCount.put(aircraftType, aircraftTypeCount.getOrDefault(aircraftType, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : aircraftTypeCount.entrySet()) {
            dataset.addValue(entry.getValue(), "Accidents", entry.getKey());
        }

        return dataset;
    }

    private DefaultPieDataset createPieDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Integer> aircraftTypeCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("javacac/src/main/java/com/example/Cleaned_Airplane2.csv"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String aircraftType = fields[6]; // Assuming aircraft type is the 7th column

                aircraftTypeCount.put(aircraftType, aircraftTypeCount.getOrDefault(aircraftType, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : aircraftTypeCount.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Aircraft Types Involved in Accidents",
                "Aircraft Type",
                "Number of Accidents",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
    }

    private JFreeChart createPieChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Proportion of Aircraft Types in Accidents",
                dataset,
                true, true, false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Ford 5-AT-C Tri Motor", new Color(200, 100, 90));
        plot.setSectionPaint("Fokker F10A Trimotor", new Color(100, 200, 90));
        plot.setSectionPaint("Stinson SM-6000B", new Color(90, 100, 200));
        plot.setSectionPaint("Douglas DC-2-112", new Color(200, 200, 90));
        plot.setSectionPaint("Stinson Model A", new Color(90, 200, 200));
        plot.setSectionPaint("Boeing 247D", new Color(200, 90, 200));
        plot.setSectionPaint("Douglas DC-2-120", new Color(150, 150, 150));
        plot.setSectionPaint("Lockheed 10B Electra", new Color(200, 150, 100));
        plot.setSectionPaint("Douglas DC-3A", new Color(100, 150, 200));
        plot.setSectionPaint("Zeppelin LZ-129", new Color(150, 100, 150));

        return chart;
    }

    public static void main(String[] args) {
        AircraftTypeAnalysis chart = new AircraftTypeAnalysis("Aviation Accident Analysis");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}