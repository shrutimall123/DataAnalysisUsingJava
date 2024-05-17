package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class AccidentAnalysis extends ApplicationFrame {

    public AccidentAnalysis(String title) {
        super(title);

        // Load data
        List<Date> accidentDates = loadData("javacac/src/main/java/com/example/Cleaned_Airplane2.csv");

        // Create datasets
        TimeSeriesCollection yearlyDataset = createYearlyDataset(accidentDates);
        DefaultCategoryDataset monthlyDataset = createMonthlyDataset(accidentDates);

        // Create charts
        JFreeChart yearlyChart = createLineChart(yearlyDataset, "Accidents Per Year", "Year", "Number of Accidents");
        JFreeChart monthlyChart = createBarChart(monthlyDataset, "Accidents Per Month", "Month", "Number of Accidents");

        // Display charts
        ChartPanel yearlyChartPanel = new ChartPanel(yearlyChart);
        ChartPanel monthlyChartPanel = new ChartPanel(monthlyChart);
        yearlyChartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        monthlyChartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        getContentPane().add(yearlyChartPanel);
        getContentPane().add(monthlyChartPanel);
    }

    private List<Date> loadData(String fileName) {
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] fields = line.split("\t");
                dates.add(sdf.parse(fields[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates;
    }

    private TimeSeriesCollection createYearlyDataset(List<Date> dates) {
        TimeSeries series = new TimeSeries("Accidents");
        Map<Integer, Integer> yearlyData = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        for (Date date : dates) {
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            yearlyData.put(year, yearlyData.getOrDefault(year, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : yearlyData.entrySet()) {
            series.add(new org.jfree.data.time.Year(entry.getKey()), entry.getValue());
        }
        return new TimeSeriesCollection(series);
    }

    private DefaultCategoryDataset createMonthlyDataset(List<Date> dates) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> monthlyData = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        for (Date date : dates) {
            String month = monthFormat.format(date);
            monthlyData.put(month, monthlyData.getOrDefault(month, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : monthlyData.entrySet()) {
            dataset.addValue(entry.getValue(), "Accidents", entry.getKey());
        }
        return dataset;
    }

    private JFreeChart createLineChart(TimeSeriesCollection dataset, String title, String xAxisLabel, String yAxisLabel) {
        return ChartFactory.createTimeSeriesChart(title, xAxisLabel, yAxisLabel, dataset, true, true, false);
    }

    private JFreeChart createBarChart(DefaultCategoryDataset dataset, String title, String xAxisLabel, String yAxisLabel) {
        return ChartFactory.createBarChart(title, xAxisLabel, yAxisLabel, dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    public static void main(String[] args) {
        AccidentAnalysis app = new AccidentAnalysis("Aviation Accident Analysis");
        app.pack();
        RefineryUtilities.centerFrameOnScreen(app);
        app.setVisible(true);
    }
}
