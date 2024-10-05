# Max Releases Validator

## Overview

The **AlphaSense-Release** is a Java application designed to assist QA engineers in selecting the maximum number of software releases that can be validated within a 10-day sprint. 
The program adheres to Agile principles and ensures that validations are conducted sequentially without overlaps. In the first version releases are being started to validate by the delivery day.
In the second version QA is allowed to postpone the release. So, he/she can move the releases by moving releases to the next available free days as needed.

## Features

- **Input Handling**: Reads release data from a text file.
- **Validation Logic**: Calculates the maximum number of non-overlapping releases that can be validated based on delivery days and validation durations.
- **Dynamic Scheduling**: In the second version adjusts the scheduling of releases to ensure they start only after their delivery day and fit into available slots.
- **Output Generation**: Writes the results, including the maximum number of valid releases and their respective validation periods, to an output file.

## Requirements

- Java Development Kit (JDK) 8 or higher
- A text editor or IDE for editing Java files

## Installation

1. Clone this repository or download the source code files.
2. Ensure you have JDK installed on your machine.
3. Place your input file named `releases.txt` in the same directory as the Java source file.

## Input Format

The input file `releases.txt` should contain multiple lines, each with two integers separated by a space:
- The first integer represents the day of the sprint (1-10) when a release is delivered.
- The second integer represents the number of days required to validate that release.

### Example Input for V1
1 1
2 1
3 1
9 1
10 4
10 2
9 5
10 3
4 5

## Example Output for V1
5
1 1
2 2
3 3
4 8
9 9


### Example Input for V2

1 1
1 2
5 1
5 3
9 1
6 1

## Example Output for V2
6
1 1
2 3
5 5
6 8
9 9
10 10