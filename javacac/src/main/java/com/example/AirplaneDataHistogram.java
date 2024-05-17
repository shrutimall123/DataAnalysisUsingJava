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
import java.util.ArrayList;
import java.util.List;

public class AirplaneDataHistogram extends JFrame {
    private DefaultCategoryDataset dataset;
    private List<Integer> passengersList;
    private List<Integer> crewList;
    private List<Integer> fatalitiesList;

    public AirplaneDataHistogram() {
        super("Airplane Data Histogram");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dataset = new DefaultCategoryDataset();
        passengersList = new ArrayList<>();
        crewList = new ArrayList<>();
        fatalitiesList = new ArrayList<>();

        try {
            // Read CSV file
            FileReader fileReader = new FileReader("javacac/src/main/java/com/example/Cleaned_Airplane2.csv");
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);

            // Extract columns
            for (CSVRecord record : parser) {
                String date = record.get("Date");
                int passengers = Integer.parseInt(record.get("Aboard Passangers")); // Corrected column name
                int crew = Integer.parseInt(record.get("Aboard Crew"));
                int fatalities = Integer.parseInt(record.get("Fatalities"));
                int fatalitiesPassengers = Integer.parseInt(record.get("Fatalities Passangers"));
                int fatalitiesCrew = Integer.parseInt(record.get("Fatalities Crew"));

                passengersList.add(passengers);
                crewList.add(crew);
                fatalitiesList.add(fatalities);

                dataset.addValue(passengers, "Passengers", date);
                dataset.addValue(crew, "Crew", date);
                dataset.addValue(fatalities, "Fatalities", date);
                dataset.addValue(fatalitiesPassengers, "Fatalities Passengers", date);
                dataset.addValue(fatalitiesCrew, "Fatalities Crew", date);
            }

            // Close CSV parser
            parser.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading CSV file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create and set up the panel
        JFreeChart chart = ChartFactory.createBarChart(
                "Airplane Data Histogram",
                "Date",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);

        // Create summary panel
        JPanel summaryPanel = createSummaryPanel();

        // Set up the content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chartPanel, BorderLayout.CENTER);
        contentPane.add(summaryPanel, BorderLayout.SOUTH);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        double passengersMean = calculateMean(passengersList);
        double passengersMedian = calculateMedian(passengersList);
        double passengersStdDev = calculateStandardDeviation(passengersList, passengersMean);

        double crewMean = calculateMean(crewList);
        double crewMedian = calculateMedian(crewList);
        double crewStdDev = calculateStandardDeviation(crewList, crewMean);

        double fatalitiesMean = calculateMean(fatalitiesList);
        double fatalitiesMedian = calculateMedian(fatalitiesList);
        double fatalitiesStdDev = calculateStandardDeviation(fatalitiesList, fatalitiesMean);

        panel.add(new JLabel(String.format("Passengers - Mean: %.2f, Median: %.2f, Std Dev: %.2f", passengersMean, passengersMedian, passengersStdDev)));
        panel.add(new JLabel(String.format("Crew - Mean: %.2f, Median: %.2f, Std Dev: %.2f", crewMean, crewMedian, crewStdDev)));
        panel.add(new JLabel(String.format("Fatalities - Mean: %.2f, Median: %.2f, Std Dev: %.2f", fatalitiesMean, fatalitiesMedian, fatalitiesStdDev)));

        return panel;
    }

    private double calculateMean(List<Integer> list) {
        double sum = 0.0;
        for (int num : list) {
            sum += num;
        }
        return sum / list.size();
    }

    private double calculateMedian(List<Integer> list) {
        List<Integer> sortedList = new ArrayList<>(list);
        sortedList.sort(Integer::compareTo);
        int middle = sortedList.size() / 2;
        if (sortedList.size() % 2 == 0) {
            return (sortedList.get(middle - 1) + sortedList.get(middle)) / 2.0;
        } else {
            return sortedList.get(middle);
        }
    }

    private double calculateStandardDeviation(List<Integer> list, double mean) {
        double sum = 0.0;
        for (int num : list) {
            sum += Math.pow(num - mean, 2);
        }
        return Math.sqrt(sum / list.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirplaneDataHistogram ex = new AirplaneDataHistogram();
            ex.setVisible(true);
        });
    }
}
