package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();
        System.out.println("Enter number of processes: ");
        int numOfProcesses = scanner.nextInt();
        System.out.println("Enter context switching time:");
        int contextSwitchTime = scanner.nextInt();

        for (int i = 0; i < numOfProcesses; i++) {
            System.out.println("Enter details of process " + (i + 1) + ": ");

            scanner.nextLine();
            System.out.println("Enter name of Process:");
            String name = scanner.nextLine();
            System.out.println("Enter Arrival Time:");
            int arrivalTime = scanner.nextInt();
            System.out.println("Enter Burst Time:");
            int burstTime = scanner.nextInt();
            System.out.println("Enter Priority:");
            int priority = scanner.nextInt();
            System.out.println("Enter Quantum Time:");
            int quantumTime = scanner.nextInt();
            System.out.println("Enter color for process " + name + " :");
            String colorInput = scanner.next();

            Color color = parseColor(colorInput); // Parse the input string into a Color object


            processes.add(new Process(name, arrivalTime, burstTime, priority, color));
            processes.get(i).setQuantum(quantumTime);
            processes.get(i).setOldQuantum(quantumTime);//initialize the old quantum as the first quantum of the process
        }
        System.out.println("Enter which Scheduler to use:\n" +
                "1-Priority Scheduling\n" +
                "2-Shortest Remaining Time First Scheduling\n" +
                "3-Shortest Job First Scheduling\n" +
                "4-FCAI Scheduling");
        int option = scanner.nextInt();
        switch (option) {
            case 1:
                NonPreemptivePriorityScheduler Priority = new NonPreemptivePriorityScheduler();
                Priority.schedule(processes, contextSwitchTime);
                break;
            case 2:
                SRTFScheduler SRTF = new SRTFScheduler(processes, contextSwitchTime);
                SRTF.runSimulation();
                break;
            case 3:
                SJFScheduler SJF = new SJFScheduler(processes, contextSwitchTime);
                SJF.runSimulation();
                break;
            case 4:
                FCAIScheduler fcaiScheduler = new FCAIScheduler(processes);
                fcaiScheduler.schedule();
                break;
            default:
                System.out.println("Invalid option");
                break;

        }
        scanner.close();
    }

    // Method to convert string color to Color object
    private static Color parseColor(String colorInput) {
        switch (colorInput.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.ORANGE;
            case "purple":
                return Color.MAGENTA;
            case "black":
                return Color.BLACK;
            case "white":
                return Color.WHITE;
            case "gray":
                return Color.GRAY;
            default:
                return Color.RED; // Default color if input is invalid
        }
    }
}