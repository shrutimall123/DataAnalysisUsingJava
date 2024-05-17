package com.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class OverllSummary {
    // ANSI color codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        String csvFilePath = "javacac/src/main/java/com/example/Cleaned_Airplane2.csv";  // Update the path to your CSV file

        try (Reader reader = new FileReader(csvFilePath)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);

            DescriptiveStatistics aboardStats = new DescriptiveStatistics();
            DescriptiveStatistics passengersStats = new DescriptiveStatistics();
            DescriptiveStatistics crewStats = new DescriptiveStatistics();
            DescriptiveStatistics fatalitiesStats = new DescriptiveStatistics();
            DescriptiveStatistics fatalitiesPassengersStats = new DescriptiveStatistics();
            DescriptiveStatistics fatalitiesCrewStats = new DescriptiveStatistics();
            DescriptiveStatistics groundStats = new DescriptiveStatistics();

            for (CSVRecord record : records) {
                aboardStats.addValue(Double.parseDouble(record.get("Aboard")));
                passengersStats.addValue(Double.parseDouble(record.get("Aboard Passangers")));
                crewStats.addValue(Double.parseDouble(record.get("Aboard Crew")));
                fatalitiesStats.addValue(Double.parseDouble(record.get("Fatalities")));
                fatalitiesPassengersStats.addValue(Double.parseDouble(record.get("Fatalities Passangers")));
                fatalitiesCrewStats.addValue(Double.parseDouble(record.get("Fatalities Crew")));
                groundStats.addValue(Double.parseDouble(record.get("Ground")));
            }
            System.out.println(ANSI_CYAN + "--------------------------------------" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "Overall Summary Statistics:" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "--------------------------------------" + ANSI_RESET);
            printStatistics("Aboard", aboardStats);
            printStatistics("Aboard Passangers", passengersStats);
            printStatistics("Aboard Crew", crewStats);
            printStatistics("Fatalities", fatalitiesStats);
            printStatistics("Fatalities Passangers", fatalitiesPassengersStats);
            printStatistics("Fatalities Crew", fatalitiesCrewStats);
            printStatistics("Ground", groundStats);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printStatistics(String columnName, DescriptiveStatistics stats) {
        System.out.println(ANSI_CYAN + "Statistics for " + columnName + ":" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Mean: " + stats.getMean() + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "Variance: " + stats.getVariance() + ANSI_RESET);
        System.out.println(ANSI_RED + "Standard Deviation: " + stats.getStandardDeviation() + ANSI_RESET);
        System.out.println();
    }
}
