package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AccidentSeverityAnalysis extends ApplicationFrame {

    public AccidentSeverityAnalysis(String title) {
        super(title);

        // Load data
        List<Integer> totalFatalities = new ArrayList<>();
        List<Integer> passengerFatalities = new ArrayList<>();
        List<Integer> crewFatalities = new ArrayList<>();
        List<Double> fatalityProportions = new ArrayList<>();
        loadData("javacac/src/main/java/com/example/Cleaned_Airplane2.csv", totalFatalities, passengerFatalities, crewFatalities, fatalityProportions);

        // Create datasets
        HistogramDataset totalFatalitiesDataset = createHistogramDataset(totalFatalities, "Total Fatalities");
        HistogramDataset passengerFatalitiesDataset = createHistogramDataset(passengerFatalities, "Passenger Fatalities");
        HistogramDataset crewFatalitiesDataset = createHistogramDataset(crewFatalities, "Crew Fatalities");
        DefaultBoxAndWhiskerCategoryDataset boxPlotDataset = createBoxPlotDataset(totalFatalities, passengerFatalities, crewFatalities);
        HistogramDataset fatalityProportionDataset = createHistogramDataset(fatalityProportions, "Fatality Proportion");

        // Create charts
        JFreeChart totalFatalitiesChart = createHistogramChart(totalFatalitiesDataset, "Total Fatalities Distribution");
        JFreeChart passengerFatalitiesChart = createHistogramChart(passengerFatalitiesDataset, "Passenger Fatalities Distribution");
        JFreeChart crewFatalitiesChart = createHistogramChart(crewFatalitiesDataset, "Crew Fatalities Distribution");
        JFreeChart boxPlotChart = createBoxPlotChart(boxPlotDataset, "Fatalities Box Plot");
        JFreeChart fatalityProportionChart = createHistogramChart(fatalityProportionDataset, "Fatality Proportion Distribution");

        // Display charts
        ChartPanel totalFatalitiesPanel = new ChartPanel(totalFatalitiesChart);
        ChartPanel passengerFatalitiesPanel = new ChartPanel(passengerFatalitiesChart);
        ChartPanel crewFatalitiesPanel = new ChartPanel(crewFatalitiesChart);
        ChartPanel boxPlotPanel = new ChartPanel(boxPlotChart);
        ChartPanel fatalityProportionPanel = new ChartPanel(fatalityProportionChart);

        totalFatalitiesPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        passengerFatalitiesPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        crewFatalitiesPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        boxPlotPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        fatalityProportionPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        getContentPane().setLayout(new java.awt.GridLayout(3, 2));
        getContentPane().add(totalFatalitiesPanel);
        getContentPane().add(passengerFatalitiesPanel);
        getContentPane().add(crewFatalitiesPanel);
        getContentPane().add(boxPlotPanel);
        getContentPane().add(fatalityProportionPanel);
    }

    private void loadData(String fileName, List<Integer> totalFatalities, List<Integer> passengerFatalities, List<Integer> crewFatalities, List<Double> fatalityProportions) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 14) {
                    try {
                        int fatalities = Integer.parseInt(fields[11].trim());
                        int passengerFatalitiesCount = Integer.parseInt(fields[12].trim());
                        int crewFatalitiesCount = Integer.parseInt(fields[13].trim());
                        int aboard = Integer.parseInt(fields[8].trim());

                        totalFatalities.add(fatalities);
                        passengerFatalities.add(passengerFatalitiesCount);
                        crewFatalities.add(crewFatalitiesCount);

                        double fatalityProportion = aboard > 0 ? (double) fatalities / aboard : 0.0;
                        fatalityProportions.add(fatalityProportion);
                    } catch (NumberFormatException e) {
                        // Skip lines with invalid number format
                        System.err.println("Skipping line due to invalid number format: " + line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HistogramDataset createHistogramDataset(List<? extends Number> data, String key) {
        HistogramDataset dataset = new HistogramDataset();
        if (data.isEmpty()) {
            System.err.println("Warning: No data available for " + key);
            return dataset;
        }
        double[] values = data.stream().mapToDouble(Number::doubleValue).toArray();
        dataset.addSeries(key, values, 10);
        return dataset;
    }

    private DefaultBoxAndWhiskerCategoryDataset createBoxPlotDataset(List<Integer> totalFatalities, List<Integer> passengerFatalities, List<Integer> crewFatalities) {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(totalFatalities, "Total Fatalities", "Fatalities");
        dataset.add(passengerFatalities, "Passenger Fatalities", "Fatalities");
        dataset.add(crewFatalities, "Crew Fatalities", "Fatalities");
        return dataset;
    }

    private JFreeChart createHistogramChart(HistogramDataset dataset, String title) {
        return ChartFactory.createHistogram(title, "Value", "Frequency", dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    private JFreeChart createBoxPlotChart(DefaultBoxAndWhiskerCategoryDataset dataset, String title) {
        return ChartFactory.createBoxAndWhiskerChart(title, "Category", "Value", dataset, true);
    }

    public static void main(String[] args) {
        AccidentSeverityAnalysis app = new AccidentSeverityAnalysis("Aviation Accident Severity Analysis");
        app.pack();
        RefineryUtilities.centerFrameOnScreen(app);
        app.setVisible(true);
    }
}
