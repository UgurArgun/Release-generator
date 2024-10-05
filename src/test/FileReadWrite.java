package test;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReadWrite {
        Logger logger = Logger.getLogger(FileReadWrite.class);
    public static void main(String[] args) throws FileNotFoundException {
        logger.info("");
        String inputFilePath = "releases.txt";
        String outputFilePath = "solution.txt";
        try {
            List<Release> selectedReleases = getMaxReleases(inputFilePath);
            writeOutput(outputFilePath, selectedReleases);
        } catch (IOException e) {
            System.err.println(STR."Error reading or writing files: \{e.getMessage()}");
        }
    }

    static List<Release> getMaxReleases(String filePath) throws FileNotFoundException {

        List<Release> releases = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner sc = new Scanner(file);
            int index = 0;
            while (sc.hasNextLine()) {
                index++;
                String line = sc.nextLine();
                System.out.println("line " + index + "= " + line);
                // Parse the line to extract deliveryDay and validationDays...
                String[] parts = line.split(" ");
                int deliveryDay = Integer.parseInt(parts[0]);
                int validationDays = Integer.parseInt(parts[1]);

                // Only consider valid releases that can be completed within 10 days and provided integers are guaranteed

                if (deliveryDay + validationDays - 1 <= 10 && deliveryDay >= 1 && validationDays >= 1) {
                    releases.add(new Release(deliveryDay, validationDays));
                } else {
                    System.out.println("Provided line " + (index) + " is invalid, as it exceeds the 10-day deadline or provided day numbers are invalid.");
                }

            }   // Sort releases by iteration according to their end day
            releases.sort(Comparator.comparingInt(Release::getEndDay));
            for (Release release : releases) {
                System.out.println("Sorted last release day = " + release.deliveryDay);

            }
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

    public static void writeOutput(String filePath, List<Release> selectedReleases) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(selectedReleases.size())); // Writes the max number of releases to the solution file
            writer.newLine();
            for (Release release : selectedReleases) {
                writer.write(release.deliveryDay + " " + release.getEndDay()); // Write delivery and end days
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println(STR."Error has been occurred while writing = \{e.getMessage()}");
            e.printStackTrace();
        }
    }
}
