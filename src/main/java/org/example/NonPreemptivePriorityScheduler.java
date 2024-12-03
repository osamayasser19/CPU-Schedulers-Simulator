package org.example;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class NonPreemptivePriorityScheduler {
    public void schedule(List<Process> processes, int contextSwitchTime) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> scheduledProcesses = new ArrayList<>();
        while (!processes.isEmpty()) {
            int finalCurrentTime = currentTime;
            Process nextProcess = processes.stream()
                    .filter(p -> p.arrivalTime <= finalCurrentTime)
                    .min(Comparator.comparingInt(p -> p.priority))
                    .orElse(null);
            if (nextProcess == null) {
                currentTime++;
                continue;
            }
            processes.remove(nextProcess);
            if (!scheduledProcesses.isEmpty()) {
                currentTime += contextSwitchTime; // 4+2=6//9+2=11
            }
            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;//6-1=5//11-2=9
            currentTime += nextProcess.burstTime;//4//6+3=9//11+2=13
            nextProcess.turnRoundTime = currentTime - nextProcess.arrivalTime;//4//9-1=8//13-2=11

            scheduledProcesses.add(nextProcess);
            System.out.println("Process " + nextProcess.name + " start in time "
                    + (currentTime - nextProcess.burstTime) + " and finish in time " + currentTime);
        }
        printResults(scheduledProcesses);
    }
    private void printResults(List<Process> processes) {
        System.out.println("\n results:");
        int totalWaitingTime = 0;
        int totalturnRoundTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalturnRoundTime += process.turnRoundTime;
            System.out.println("Process " + process.name + ": Waiting Time = "
                    + process.waitingTime + ", Turnaround Time = " + process.turnRoundTime);
        }
        System.out.println("average Waiting Time= " + (double) totalWaitingTime / processes.size());
        System.out.println("average TurnRound Time = " + (double) totalturnRoundTime / processes.size());
    }
}


