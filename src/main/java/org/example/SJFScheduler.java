package org.example;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class SJFScheduler {
    private static List<Process> processes;
    private int contextSwitchTime;

    public SJFScheduler(List<Process> processes1, int contextSwitchTime) {
        this.processes = processes1;
        this.contextSwitchTime = contextSwitchTime;
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
    }

    public static void runSimulation() {
        int currentTime = 0;
        int completed = 0;
        List<String> executionOrder = new ArrayList<>();

        // Simulation loop
        while (completed < processes.size()) {
            // Select the process with the shortest burst time that has arrived and not completed
            Process nextProcess = null;
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && process.getRemainingBurstTime() > 0) {
                    if (nextProcess == null || process.getBurstTime() < nextProcess.getBurstTime()) {
                        nextProcess = process;
                    }
                }
            }

            // Execute the selected process
            if (nextProcess != null) {
                executionOrder.add(nextProcess.getName()); // Add to execution order
                currentTime = Math.max(currentTime, nextProcess.getArrivalTime()); // Wait if process hasn’t arrived yet
                currentTime += nextProcess.getBurstTime(); // Execute process until completion

                nextProcess.setRemainingBurstTime(0); // Mark as completed
                nextProcess.setCompletionTime(currentTime);
                nextProcess.setTurnRoundTime(currentTime - nextProcess.getArrivalTime());
                nextProcess.setWaitingTime(nextProcess.getTurnRoundTime() - nextProcess.getBurstTime());

                completed++;
            } else {
                currentTime++; // If no process is ready, increment time
            }
        }

        printResults(executionOrder);
        visualizeExecutionOrder(executionOrder);
    }

    private static void printResults(List<String> executionOrder) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        // Calculate averages
        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnRoundTime();
        }

        double averageWaitingTime = totalWaitingTime / processes.size();
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();

        // Output results
        System.out.println("\nExecution Order: " + String.join(" -> ", executionOrder));

        System.out.println("\nProcess Details:");
        System.out.println("Name       Arrival    Burst      Waiting    Turnaround Completion");
        for (Process process : processes) {
            System.out.println(process.getName() + "         " + process.getArrivalTime() + "          " +
                    process.getBurstTime() + "          " + process.getWaitingTime() + "            " +
                    process.getTurnRoundTime() + "        " + process.getCompletionTime());
        }

        // Direct output of averages without formatting
        System.out.println("\nAverage Waiting Time: " + averageWaitingTime);
        System.out.println("Average Turnaround Time: " + averageTurnaroundTime);
    }

    private static void visualizeExecutionOrder(List<String> executionOrder) {
        JFrame frame = new JFrame("SJF Execution Order");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a panel to visualize the execution order
        JPanel executionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int xPosition = 50;
                int scaleFactor = 30; // Pixels per time unit

                for (String processName : executionOrder) {
                    Process process = findProcessByName(processName);
                    int width = process.getBurstTime() * scaleFactor;

                    g.setColor(process.color);
                    g.fillRect(xPosition, 50, width, 50);
                    g.setColor(Color.BLACK);
                    g.drawRect(xPosition, 50, width, 50);
                    g.drawString(processName, xPosition + width / 2 - 10, 80);

                    xPosition += width;
                }
            }
        };

        panel.add(executionPanel, BorderLayout.CENTER);

        // Create a table to display process details
        String[] columnNames = {"Name", "Arrival", "Burst", "Waiting", "Turnaround", "Completion"};
        Object[][] data = new Object[processes.size()][6];

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            data[i][0] = process.getName();
            data[i][1] = process.getArrivalTime();
            data[i][2] = process.getBurstTime();
            data[i][3] = process.getWaitingTime();
            data[i][4] = process.getTurnRoundTime();
            data[i][5] = process.getCompletionTime();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static Process findProcessByName(String name) {
        for (Process process : processes) {
            if (process.getName().equals(name)) {
                return process;
            }
        }
        return null;
    }
}