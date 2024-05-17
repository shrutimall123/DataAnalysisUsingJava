package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoading {
    public static void main(String[] args) {
        String csvFile = "javacac/src/main/java/com/example/Cleaned_Airplane1.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                for (String datum : data) {
                    System.out.print(datum + " | ");
                }
                System.out.println(); // Move to the next line for the next row
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

