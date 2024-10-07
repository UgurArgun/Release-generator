package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class QAReleaseValidatorWithPostpone {

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

    static List<Release> getMaxReleases(String filePath) {

        List<Release> releases = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            int index = 0;
            while (sc.hasNextLine()) {
                index++;
                String line = sc.nextLine();
                System.out.println("line " + index + "= " + line);
                String[] parts = line.split(" ");
                int deliveryDay = Integer.parseInt(parts[0]);
                int validationDays = Integer.parseInt(parts[1]);

                if (deliveryDay + validationDays - 1 <= 10 && deliveryDay >= 1 && validationDays >= 1) {
                    releases.add(new Release(deliveryDay, validationDays));
                } else {
                    System.out.println("Provided line " + (index) + " is invalid, as it exceeds the 10-day deadline or provided day numbers are invalid.");
                }

            }
            List<Release> sortedReleases = new ArrayList<>();

            while (!releases.isEmpty()) {
                Release minRelease = releases.getFirst();

                for (Release release : releases) {
                    if (release.deliveryDay < minRelease.deliveryDay || (release.deliveryDay == minRelease.deliveryDay && release.validationDays < minRelease.validationDays)) {
                        minRelease = release; // Updates minRelease if a smaller delivery day or smaller delivery day + validation days is found
                    }
                }

                sortedReleases.add(minRelease);
                releases.remove(minRelease);

            }
            System.out.println("Sorted Releases:");
            for (Release release : sortedReleases) {
                System.out.println("Delivery Day: " + release.getDeliveryDay() + ", End Day: " + release.getEndDay());
            }

            List<Release> selectedReleases = new ArrayList<>();

            boolean[] occupiedDays = new boolean[11];

            for (Release release : sortedReleases) {
                int startTime = release.deliveryDay;

                // Checks if we can schedule this release starting from its delivery day
                while (startTime <= 10 && occupiedDays[startTime]) {
                    startTime++;  // Move to the next day if occupied
                }

                // Checks if there is enough space to validate this release starting from startTime
                if (startTime <= 10 && startTime + release.validationDays - 1 <= 10) {
                    boolean canSchedule = true;

                    // Checks if all required days are free
                    for (int i = startTime; i < startTime + release.validationDays; i++) {
                        if (occupiedDays[i]) {
                            canSchedule = false;
                            break;  // Breaks if any required day is occupied
                        }
                    }

                    // If all required days are free, schedules this release
                    if (canSchedule) {
                        selectedReleases.add(new Release(startTime, release.validationDays));
                        for (int i = startTime; i < startTime + release.validationDays; i++) {
                            occupiedDays[i] = true;  // Mark these days as occupied
                        }
                    }
                }
            }

            return selectedReleases;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return releases;
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

