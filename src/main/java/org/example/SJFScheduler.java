package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SJFScheduler {

    void schedule(List<Process> processes, int contextSwitchingTime) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int completedProcesses = 0;
        List<Process> executionOrder = new ArrayList<>();
        List<String> processTimeline = new ArrayList<>();

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
                processTimeline.add("Idle");
                continue;
            }

            currentProcess.startedExecution = true;
            currentProcess.waitingTime = currentTime - currentProcess.arrivalTime;

            // Add the process name to the timeline for its burst duration
            for (int i = 0; i < currentProcess.burstTime; i++) {
                processTimeline.add(currentProcess.name);
            }

            currentTime += currentProcess.burstTime + contextSwitchingTime;
            currentProcess.turnRoundTime = currentTime - currentProcess.arrivalTime;
            currentProcess.remainingBurstTime = 0;
            executionOrder.add(currentProcess);
            completedProcesses++;
        }

        displayGanttChart(processTimeline, currentTime, processes);

        // Calculate and display statistics
        displayStatistics(processes);
    }

    private void displayGanttChart(List<String> processTimeline, int totalTime, List<Process> processes) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Process Scheduling Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 300);
            frame.setLayout(new BorderLayout());

            JPanel ganttPanel = new JPanel();
            ganttPanel.setLayout(null); // Use absolute positioning
            int currentX = 50; // Start position for the bars

            // Define colors for each process
            Map<String, Color> processColors = new HashMap<>();
            processColors.put("p1", Color.CYAN);
            processColors.put("p2", Color.YELLOW);
            processColors.put("p3", new Color(165, 42, 42)); // Brown
            processColors.put("p4", Color.BLUE);
            processColors.put("p5", Color.GREEN);
            processColors.put("p6", new Color(128, 0, 128)); // Purple

            for (String processName : processTimeline) {
                if (processName.equals("Idle")) {
                    currentX += 20; // Adjust the idle width
                    continue;
                }

                Process process = processes.stream()
                        .filter(p -> p.name.equals(processName))
                        .findFirst()
                        .orElse(null);

                if (process != null) {
                    int width = process.burstTime * 20; // Width based on burst time

                    // Create a panel for each process
                    JPanel processPanel = new JPanel();
                    processPanel.setBackground(processColors.get(processName));
                    processPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    processPanel.setBounds(currentX, 100, width, 40); // Set position and size

                    JLabel label = new JLabel(processName, JLabel.CENTER);
                    label.setForeground(Color.BLACK);
                    processPanel.add(label);
                    ganttPanel.add(processPanel);

                    currentX += width; // Move the current X position for the next process
                }
            }

            // Add time labels above the bars
            JPanel timeLabelsPanel = new JPanel();
            timeLabelsPanel.setLayout(new GridLayout(1, totalTime));
            for (int i = 0; i <= totalTime; i++) {
                timeLabelsPanel.add(new JLabel(String.valueOf(i), JLabel.CENTER));
            }

            frame.add(timeLabelsPanel, BorderLayout.NORTH);
            frame.add(ganttPanel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private void displayStatistics(List<Process> processes) {
        double totalWaitingTime = 0;
        double totalTurnAroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnAroundTime += process.turnRoundTime;
        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnAroundTime = totalTurnAroundTime / processes.size();

        System.out.println("Processes execution order:");
        for (Process process : processes) {
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
    }}
