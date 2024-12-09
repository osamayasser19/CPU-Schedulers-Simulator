package org.example;

import java.util.*;

public class SJFScheduler {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.print("Enter number of processes: ");
//        int numProcesses = scanner.nextInt();
//
//        System.out.print("Enter context switching time: ");
//        int contextSwitchingTime = scanner.nextInt();
//
//        List<Process> processes = new ArrayList<>();
//
//        for (int i = 0; i < numProcesses; i++) {
//            System.out.print("Enter process name: ");
//            String name = scanner.next();
//
//            System.out.print("Enter arrival time: ");
//            int arrivalTime = scanner.nextInt();
//
//            System.out.print("Enter burst time: ");
//            int burstTime = scanner.nextInt();
//
//            System.out.print("Enter priority: ");
//            int priority = scanner.nextInt();
//
//            processes.add(new Process(name, arrivalTime, burstTime, priority));
//        }
        void schedule (List < Process > processes,int contextSwitchingTime){
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // Implement SJF Non-Preemptive Scheduling
        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> executionOrder = new ArrayList<>();

        while (completedProcesses < processes.size()) {
            Process currentProcess = null;
            int minBurstTime = Integer.MAX_VALUE;

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && !process.startedExecution && process.remainingBurstTime < minBurstTime) {
                    minBurstTime = process.remainingBurstTime;
                    currentProcess = process;
                }
            }

            if (currentProcess == null) {
                currentTime++;
                continue;
            }

            currentProcess.startedExecution = true;
            currentProcess.waitingTime = currentTime - currentProcess.arrivalTime;
            currentTime += currentProcess.burstTime + contextSwitchingTime;
            currentProcess.turnRoundTime = currentTime - currentProcess.arrivalTime;
            currentProcess.remainingBurstTime = 0;
            executionOrder.add(currentProcess);
            completedProcesses++;
        }

        // Calculate average waiting time and turnaround time
        double totalWaitingTime = 0;
        double totalTurnAroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnRoundTime;
        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnAroundTime = totalTurnAroundTime / processes.size();

        // Output the results
        System.out.println("Processes execution order:");
        for (Process process : executionOrder) {
            System.out.println(process.name);
        }

        System.out.println("Waiting Time for each process:");
        for (Process process : processes) {
            System.out.println(process.name + ": " + process.waitingTime);
        }

        System.out.println("Turnaround Time for each process:");
        for (Process process : processes) {
            System.out.println(process.name + ": " + process.turnRoundTime);
        }

        System.out.println("Average Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnAroundTime);
        }
//    }
}
