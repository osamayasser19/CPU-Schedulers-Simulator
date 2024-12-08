package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SRTFScheduler extends Scheduler {
    private List<Process> processes;
    private int contextSwitchTime;

    public SRTFScheduler(List<Process> processes1, int contextSwitchTime) {
        this.processes = processes1;
        this.contextSwitchTime = contextSwitchTime;
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

    }
    public void runSimulation() {
        int currentTime = 0;
        int completed = 0;
        Process currentProcess = null;
        List<String> executionOrder = new ArrayList<>();

        // Simulation loop
        while (completed < processes.size()) {
            // Select the process with the shortest remaining time that has arrived
            Process nextProcess = null;
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingBurstTime > 0) {
                    if (nextProcess == null || process.remainingBurstTime < nextProcess.remainingBurstTime) {
                        nextProcess = process;
                    }
                }
            }

            if (nextProcess != currentProcess) {
                // If switching to a different process, add context switch time
                if (currentProcess != null) {
                    currentTime += contextSwitchTime;  // Add context switch time
                }
                currentProcess = nextProcess;
            }

            // Execute the current process
            if (currentProcess != null ) {
                currentProcess.remainingBurstTime--;
                executionOrder.add(currentProcess.name);
                // If the process is completed
                if (currentProcess.remainingBurstTime == 0) {
                    completed++;
                    currentProcess.completionTime = currentTime + 1;
                    currentProcess.turnRoundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnRoundTime - currentProcess.burstTime;
                }
            }

            currentTime++;  // Move time forward by 1 unit
        }

        printResults(executionOrder);
    }

    private void printResults(List<String> executionOrder) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        // Calculate averages
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnRoundTime;
        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();

        // Output results
        System.out.println("\nExecution Order: " + String.join(" -> ", executionOrder));

        System.out.println("\nProcess Details:");
        System.out.println("Name       Arrival    Burst      Waiting    Turnaround Completion");
        for (Process process : processes) {
            System.out.println(process.name + "         " + process.arrivalTime + "          " +
                    process.burstTime + "          " + process.waitingTime + "            " +
                    process.turnRoundTime + "        " + process.completionTime);
        }

        // Direct output of averages without formatting
        System.out.println("\nAverage Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }
}
