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
                String[] parts = line.split(" ");
                int deliveryDay = Integer.parseInt(parts[0]);
                int validationDays = Integer.parseInt(parts[1]);

                if (deliveryDay + validationDays - 1 <= 10 && deliveryDay >= 1 && validationDays >= 1) {
                    releases.add(new Release(deliveryDay, validationDays));
                } else {
                    System.out.println("Provided line " + (index) + " is invalid, as it exceeds the 10-day deadline or provided day numbers are invalid.");
                }

            }

            releases = sortReleasesByDeliveryAndValidation(releases);

            // Selecting maximum number of non-overlapping releases
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


    private static List<Release> sortReleasesByDeliveryAndValidation(List<Release> releases) {
        List<Release> sortedReleases = new ArrayList<>();

        while (!releases.isEmpty()) {
            Release minRelease = releases.getFirst();

            for (Release release : releases) {
                if (release.deliveryDay < minRelease.deliveryDay ||
                        (release.deliveryDay == minRelease.deliveryDay && release.validationDays < minRelease.validationDays)) {
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
        return sortedReleases;
    }

    static void writeOutput(String filePath, List<Release> selectedReleases) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(selectedReleases.size()));
            writer.newLine();
            for (Release release : selectedReleases) {
                writer.write(release.deliveryDay + " " + release.getEndDay());
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error has been occurred while writing = " + e.getMessage());
        }
    }
}
