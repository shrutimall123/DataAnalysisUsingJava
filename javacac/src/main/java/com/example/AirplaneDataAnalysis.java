package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AirplaneDataAnalysis {
    private static final String FILE_PATH = "javacac/src/main/java/com/example/Cleaned_Airplane.csv";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";

    public static void main(String[] args) {
        // Load data from CSV file
        Map<String, Integer> data = loadData(FILE_PATH);

        // Perform statistical analysis
        int totalFlights = data.getOrDefault("totalFlights", 0);
        int totalFatalities = data.getOrDefault("totalFatalities", 0);
        double averageFatalitiesPerFlight = calculateAverageFatalitiesPerFlight(totalFlights, totalFatalities);

        // Display results with ASCII colors
        System.out.println(ANSI_CYAN + "Statistical Analysis of Airplane Data" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "--------------------------------------" + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Total number of flights: " + totalFlights + ANSI_RESET);
        System.out.println(ANSI_RED + "Total number of fatalities: " + totalFatalities + ANSI_RESET);
        System.out.println(ANSI_GREEN + "Average fatalities per flight: " + averageFatalitiesPerFlight + ANSI_RESET);
    }

    /**
     * Load data from the CSV file.
     *
     * @param filePath The path to the CSV file.
     * @return A map containing statistical data.
     */
    private static Map<String, Integer> loadData(String filePath) {
        Map<String, Integer> data = new HashMap<>();
        int totalFlights = 0;
        int totalFatalities = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 13) {
                    try {
                        double fatalities = Double.parseDouble(parts[13]);
                        totalFlights++;
                        totalFatalities += (int) fatalities; // Convert double to int
                    } catch (NumberFormatException e) {
                        // Skip rows with non-numeric fatalities
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        data.put("totalFlights", totalFlights);
        data.put("totalFatalities", totalFatalities);
        return data;
    }

    /**
     * Calculate the average number of fatalities per flight.
     *
     * @param totalFlights The total number of flights.
     * @param totalFatalities The total number of fatalities.
     * @return The average number of fatalities per flight.
     */
    private static double calculateAverageFatalitiesPerFlight(int totalFlights, int totalFatalities) {
        if (totalFlights == 0) {
            return 0;
        }
        return (double) totalFatalities / totalFlights;
    }
}