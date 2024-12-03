package org.example;

import java.util.*;

public class SJFScheduler {

    // Method to calculate Waiting Time and Turnaround Time for each process
    public static void calculateTimes(List<Process> processes) {
        int totalTime = 0;
        for (Process process : processes) {
            totalTime += process.getBurstTime();
        }

        int waitingTimeSum = 0;
        int turnaroundTimeSum = 0;

        for (Process process : processes) {
            process.turnRoundTime = totalTime - process.getArrivalTime() - process.getBurstTime();
            process.waitingTime = process.turnRoundTime - process.getArrivalTime();
            waitingTimeSum += process.waitingTime;
            turnaroundTimeSum += process.turnRoundTime;
        }

        System.out.println("Average Waiting Time: " + (waitingTimeSum * 1.0 / processes.size()));
        System.out.println("Average Turnaround Time: " + (turnaroundTimeSum * 1.0 / processes.size()));
    }

    // SJF Scheduling algorithm
    public static void sjfScheduling(List<Process> processes) {
        // Sort processes based on their burst time (ascending order)
        processes.sort(Comparator.comparingInt(Process::getBurstTime));

        int currentTime = 0;
        for (Process process : processes) {
            if (process.getArrivalTime() > currentTime) {
                currentTime = process.getArrivalTime(); // Wait for the process to arrive
            }

            // Execute the process
            currentTime += process.getBurstTime();
            System.out.println("Process " + process.getName() + " executed from " + (currentTime - process.getBurstTime()) + " to " + currentTime);
        }

        // Calculate and print times (waiting time, turnaround time)
        calculateTimes(processes);
    }

    public static void main(String[] args) {
        // Create processes (name, arrivalTime, burstTime, priority)
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 6, 1));
        processes.add(new Process("P2", 1, 8, 2));
        processes.add(new Process("P3", 2, 7, 3));
        processes.add(new Process("P4", 3, 3, 4));

        // Run SJF Scheduling
        sjfScheduling(processes);
    }
}
