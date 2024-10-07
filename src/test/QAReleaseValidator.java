package test;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class QAReleaseValidator {
    public static void main(String[] args) {
        String inputFilePath = "releases.txt";
        String outputFilePath = "solution.txt";
        try {
            List<Release> selectedReleases = getMaxReleases(inputFilePath);
            writeOutput(outputFilePath, selectedReleases);
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    static List<Release> getMaxReleases(String filePath) throws FileNotFoundException {

        List<Release> releases = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            int index = 1;
            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                System.out.println("line " + index + "= " + line);
                index++;
                // Parse the line to extract deliveryDay and validationDays...
                String[] parts = line.split(" ");
                int deliveryDay = Integer.parseInt(parts[0]);
                int validationDays = Integer.parseInt(parts[1]);
                System.out.println("deliveryDay = " + deliveryDay);
                System.out.println("validationDays = " + validationDays);

                // Only consider valid releases that can be completed within 10 days and provided integers are guaranteed
                if (deliveryDay + validationDays - 1 <= 10 && deliveryDay >= 1 && validationDays >= 1) {
                    releases.add(new Release(deliveryDay, validationDays));
                } else {
                    System.out.println("Provided line " + (index) + " is invalid, as it exceeds the 10-day deadline or provided day numbers are invalid.");
                }

            }

            // Sort releases by their delivery day and validation days using a custom method
            releases = sortReleasesByDeliveryAndValidation(releases);

            // Select maximum number of non-overlapping releases
            List<Release> selectedReleases = new ArrayList<>();
            int lastEndTime = 0;

            for (Release release : releases) {
                if (release.deliveryDay > lastEndTime) {
                    selectedReleases.add(release);
                    lastEndTime = release.getEndDay();
                }
            }

            sc.close();
            return selectedReleases;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return releases;
    }

    // Method to sort releases by their delivery day and then by validation days using a for-each loop
    private static List<Release> sortReleasesByDeliveryAndValidation(List<Release> releases) {
        List<Release> sortedReleases = new ArrayList<>();

        while (!releases.isEmpty()) {
            Release minRelease = releases.get(0); // Start with the first release

            // Use a for-each loop to find the release with the minimum delivery day
            for (Release release : releases) {
                if (release.deliveryDay < minRelease.deliveryDay ||
                        (release.deliveryDay == minRelease.deliveryDay && release.validationDays < minRelease.validationDays)) {
                    minRelease = release; // Update minRelease if a smaller delivery day or smaller validation days is found
                }
            }

            // Add the found minimum release to the sorted list
            sortedReleases.add(minRelease);
            releases.remove(minRelease); // Remove it from the original list
        }

        return sortedReleases; // Return the sorted list
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
            System.out.println("Error has been occurred while writing = " + e.getMessage());
        }
    }
}
