package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

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
                if (executionOrder.isEmpty() || !executionOrder.get(executionOrder.size() - 1).equals(currentProcess.name)) {
                    executionOrder.add(currentProcess.name);
                }
                currentProcess.remainingBurstTime--;

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
        visualizeExecutionOrder(executionOrder); // Visualize the execution order

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


    private void visualizeExecutionOrder(List<String> executionOrder) {
        // Create a new JFrame to display the execution order and process details
        JFrame frame = new JFrame("SRTF Execution Order");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        frame.setSize(800, 600); // Set the size of the frame (increased to fit both graph and table)

        // Create a main panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout()); // Use BorderLayout to add multiple components

        // Create a subpanel to display the colors of the processes
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new FlowLayout()); // Arrange the colors in a horizontal row

        // Add color boxes (rectangles) for each process
        for (Process process : processes) {
            JLabel colorLabel = new JLabel();
            colorLabel.setBackground(process.color);
            colorLabel.setOpaque(true);
            colorLabel.setPreferredSize(new Dimension(50, 30));  // Fixed size for the color labels
            colorPanel.add(colorLabel);
        }

        // Add the color panel to the main panel at the top
        panel.add(colorPanel, BorderLayout.NORTH);

        // Create a panel to visualize the execution order (graph)
        JPanel executionPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Call the parent method to ensure proper painting
                int xPosition = 50; // Initial position for drawing the processes on the horizontal axis
                int scaleFactor = 10;  // You can adjust this scale factor to increase/decrease the width of the rectangles
                int spacing = 20;  // Fixed spacing between the rectangles

                // Loop through the execution order list and draw each process
                for (String processName : executionOrder) {
                    Process process = findProcessByName(processName); // Find the process based on its name
                    g.setColor(process.color); // Set the color associated with the process
                    // Calculate the width of the rectangle based on the burst time (or remaining time)
                    int width = process.burstTime * scaleFactor;
                    g.fillRect(xPosition, 50, width, 50); // Draw a filled rectangle representing the process
                    g.setColor(Color.BLACK); // Set the color to black for drawing borders and text
                    g.drawRect(xPosition, 50, width, 50); // Draw the border of the rectangle
                    g.drawString(processName, xPosition + 10, 80); // Draw the process name inside the rectangle
                    xPosition += width + spacing;  // Add fixed spacing between the rectangles
                }
            }
        };

        // Add the execution panel to the main panel
        panel.add(executionPanel, BorderLayout.CENTER); // Add execution panel in the center

        // Create the table to display process details
        String[] columnNames = {"Name", "Arrival", "Burst", "Waiting", "Turnaround", "Completion", "Color"};
        Object[][] data = new Object[processes.size()][7];

        // Fill the data array with process information
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            data[i][0] = process.name;
            data[i][1] = process.arrivalTime;
            data[i][2] = process.burstTime;
            data[i][3] = process.waitingTime;
            data[i][4] = process.turnRoundTime;
            data[i][5] = process.completionTime;
            data[i][6] = new JLabel(" ", JLabel.CENTER);  // Create an empty label to display color
            ((JLabel) data[i][6]).setBackground(process.color); // Set the background color to match the process color
            ((JLabel) data[i][6]).setOpaque(true);  // Make sure the label's background is visible
        }

        // Create a table to display the process details with color
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(750, 300)); // Set preferred size of the table (smaller size)

        // Make the table's columns more compact
        table.getColumnModel().getColumn(0).setPreferredWidth(60); // Set width of "Name" column
        table.getColumnModel().getColumn(1).setPreferredWidth(50); // Set width of "Arrival" column
        table.getColumnModel().getColumn(2).setPreferredWidth(50); // Set width of "Burst" column
        table.getColumnModel().getColumn(3).setPreferredWidth(60); // Set width of "Waiting" column
        table.getColumnModel().getColumn(4).setPreferredWidth(80); // Set width of "Turnaround" column
        table.getColumnModel().getColumn(5).setPreferredWidth(80); // Set width of "Completion" column
        table.getColumnModel().getColumn(6).setPreferredWidth(60); // Set width of "Color" column

        // Add the table to a scroll pane for better viewing
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Add the table scroll pane to the panel in the south section (below the graph)
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add the panel to the frame and make the frame visible
        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to find a process by its name
    private Process findProcessByName(String name) {
        for (Process process : processes) {
            if (process.name.equals(name)) {
                return process; // Return the process if it matches the name
            }
        }
        return null; // Should never happen if the process exists
    }
}
