package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaxReleaseValidator {
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(MaxReleaseValidator.class);
    Logger logger = Logger.getLogger(MaxReleaseValidator.class.getName());
    public static void main(String[] args) {
        logger.info("info: MaxReleaseValidator");
        String inputFilePath = "releases.txt";
        String outputFilePath = "solution.txt";
        try {
            List<Release> selectedReleases = getMaxReleases(inputFilePath);
            writeOutput(outputFilePath, selectedReleases);
        } catch (IOException e) {
            System.err.println(STR."Error reading or writing files: \{e.getMessage()}");
        }
    }

     static List<Release> getMaxReleases(String filePath) {

        List<Release> releases = new ArrayList<>();

        // Read and parse the input file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                index++;
                System.out.println("line " + index + "= " + line);
                String[] parts = line.split(" ");
                int deliveryDay = Integer.parseInt(parts[0]);
                int validationDays = Integer.parseInt(parts[1]);

                // Only consider valid releases that can be completed within 10 days and provided integers are guaranteed
                if (deliveryDay + validationDays - 1 <= 10 && deliveryDay >= 1 && validationDays >= 1) {
                    releases.add(new Release(deliveryDay, validationDays));
                } else {
                    System.out.println("Provided line " + (index) + " is invalid, as it exceeds the 10-day deadline or provided day numbers are invalid.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

         // Sort releases by their delivery day (to ensure we validate in order)
         releases.sort(Comparator.comparingInt(r -> r.deliveryDay));

         // Select maximum number of non-overlapping releases
         List<Release> selectedReleases = new ArrayList<>();

         // Array to track occupied days in the sprint (1-10)
         boolean[] occupiedDays = new boolean[11];  // Index 0 is unused for convenience

         for (Release release : releases) {
             int startTime = release.deliveryDay;

             // Check if we can schedule this release starting from its delivery day
             while (startTime <= 10 && occupiedDays[startTime]) {
                 startTime++;  // Move to the next day if occupied
             }

             // Check if there is enough space to validate this release starting from startTime
             if (startTime <= 10 && startTime + release.validationDays - 1 <= 10) {
                 boolean canSchedule = true;

                 // Check if all required days are free
                 for (int i = startTime; i < startTime + release.validationDays; i++) {
                     if (occupiedDays[i]) {
                         canSchedule = false;
                         break;  // Break if any required day is occupied
                     }
                 }

                 // If all required days are free, schedule this release
                 if (canSchedule) {
                     selectedReleases.add(new Release(startTime, release.validationDays));
                     for (int i = startTime; i < startTime + release.validationDays; i++) {
                         occupiedDays[i] = true;  // Mark these days as occupied
                     }
                 }
             }
         }

         return selectedReleases;
     }
    static void writeOutput(String filePath, List<Release> selectedReleases) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(selectedReleases.size())); // Writes the max number of releases to the solution file
            writer.newLine();
            for (Release release : selectedReleases) {
                writer.write(release.deliveryDay + " " + release.getEndDay()); // Write delivery and end days
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println(STR."Error has been occurred while writing = \{e.getMessage()}");
        }
    }
}

