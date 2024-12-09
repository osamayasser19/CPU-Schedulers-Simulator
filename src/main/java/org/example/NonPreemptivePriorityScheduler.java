package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NonPreemptivePriorityScheduler {
    public void schedule(List<Process> processes, int contextSwitchTime) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<Process> scheduledProcesses = new ArrayList<>();
        List<ExecutionStep> executionSteps = new ArrayList<>();

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
                currentTime += contextSwitchTime;
            }

            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;
            currentTime += nextProcess.burstTime;
            nextProcess.turnRoundTime = currentTime - nextProcess.arrivalTime;

            scheduledProcesses.add(nextProcess);
            executionSteps.add(new ExecutionStep(nextProcess, currentTime - nextProcess.burstTime, currentTime));

            System.out.println("Process " + nextProcess.name + " starts at time "
                    + (currentTime - nextProcess.burstTime) + " and finishes at time " + currentTime);
        }

        printResults(scheduledProcesses);
        displayGanttChart(executionSteps);
    }

    private void printResults(List<Process> processes) {
        System.out.println("\nResults:");
        int totalWaitingTime = 0;
        int totalTurnRoundTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnRoundTime += process.turnRoundTime;
            System.out.println("Process " + process.name + ": Waiting Time = "
                    + process.waitingTime + ", Turnaround Time = " + process.turnRoundTime);
        }
        System.out.println("Average Waiting Time = " + (double) totalWaitingTime / processes.size());
        System.out.println("Average Turnaround Time = " + (double) totalTurnRoundTime / processes.size());
    }

    private void displayGanttChart(List<ExecutionStep> steps) {
        JFrame frame = new JFrame("Gantt Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                int x = 50; // Starting x position
                int y = 50; // Vertical position of bars
                int height = 50; // Height of each bar

                for (ExecutionStep step : steps) {
                    g.setColor(step.process.color);
                    int width = (step.endTime - step.startTime) * 20; // Proportional to burst time
                    g.fillRect(x, y, width, height);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, width, height); // Outline for visibility
                    g.drawString(step.process.name + " (" + step.startTime + "-" + step.endTime + ")",
                            x + 10, y + 40);
                    x += width;
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }

    private static class ExecutionStep {
        Process process;
        int startTime, endTime;

        public ExecutionStep(Process process, int startTime, int endTime) {
            this.process = process;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
//package org.example;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//public class NonPreemptivePriorityScheduler {
//    public void schedule(List<Process> processes, int contextSwitchTime) {
//        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
//        int currentTime = 0;
//        List<Process> scheduledProcesses = new ArrayList<>();
//        while (!processes.isEmpty()) {
//            int finalCurrentTime = currentTime;
//            Process nextProcess = processes.stream()
//                    .filter(p -> p.arrivalTime <= finalCurrentTime)
//                    .min(Comparator.comparingInt(p -> p.priority))
//                    .orElse(null);
//            if (nextProcess == null) {
//                currentTime++;
//                continue;
//            }
//            processes.remove(nextProcess);
//            if (!scheduledProcesses.isEmpty()) {
//                currentTime += contextSwitchTime; // 4+2=6//9+2=11
//            }
//            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;//6-1=5//11-2=9
//            currentTime += nextProcess.burstTime;//4//6+3=9//11+2=13
//            nextProcess.turnRoundTime = currentTime - nextProcess.arrivalTime;//4//9-1=8//13-2=11
//
//            scheduledProcesses.add(nextProcess);
//            System.out.println("Process " + nextProcess.name + " start in time "
//                    + (currentTime - nextProcess.burstTime) + " and finish in time " + currentTime);
//        }
//        printResults(scheduledProcesses);
//    }
//    private void printResults(List<Process> processes) {
//        System.out.println("\n results:");
//        int totalWaitingTime = 0;
//        int totalturnRoundTime = 0;
//        for (Process process : processes) {
//            totalWaitingTime += process.waitingTime;
//            totalturnRoundTime += process.turnRoundTime;
//            System.out.println("Process " + process.name + ": Waiting Time = "
//                    + process.waitingTime + ", Turnaround Time = " + process.turnRoundTime);
//        }
//        System.out.println("average Waiting Time= " + (double) totalWaitingTime / processes.size());
//        System.out.println("average TurnRound Time = " + (double) totalturnRoundTime / processes.size());
//    }
//}
//
//
