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
        displayGanttChart(executionSteps, scheduledProcesses);
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

    private void displayGanttChart(List<ExecutionStep> steps, List<Process> processes) {
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
                            x + 20, y + 30);
                    x += width;
                }
            }
        };

        // Create the table to display process details
        String[] columnNames = {"Name", "Arrival", "Burst", "Waiting", "Turnaround", "Color"};
        Object[][] data = new Object[processes.size()][6];

        // Fill the data array with process information
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            data[i][0] = process.name;
            data[i][1] = process.arrivalTime;
            data[i][2] = process.burstTime;
            data[i][3] = process.waitingTime;
            data[i][4] = process.turnRoundTime;
//            data[i][5] = process.turnRoundTime + process.arrivalTime; // Completion time

            JLabel colorLabel = new JLabel();
            colorLabel.setBackground(process.color);
            colorLabel.setOpaque(true); // Ensure color is visible
            data[i][5] = colorLabel;
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
//        table.getColumnModel().getColumn(6).setPreferredWidth(60); // Set width of "Color" column

        // Add the table to a scroll pane for better viewing
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Add the table scroll pane to the panel in the south section (below the graph)
        panel.setLayout(new BorderLayout());
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add the panel to the frame and make the frame visible
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
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class NonPreemptivePriorityScheduler {
//    public void schedule(List<Process> processes, int contextSwitchTime) {
//        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
//        int currentTime = 0;
//        List<Process> scheduledProcesses = new ArrayList<>();
//        List<ExecutionStep> executionSteps = new ArrayList<>();
//
//        while (!processes.isEmpty()) {
//            int finalCurrentTime = currentTime;
//            Process nextProcess = processes.stream()
//                    .filter(p -> p.arrivalTime <= finalCurrentTime)
//                    .min(Comparator.comparingInt(p -> p.priority))
//                    .orElse(null);
//
//            if (nextProcess == null) {
//                currentTime++;
//                continue;
//            }
//
//            processes.remove(nextProcess);
//            if (!scheduledProcesses.isEmpty()) {
//                currentTime += contextSwitchTime;
//            }
//
//            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;
//            currentTime += nextProcess.burstTime;
//            nextProcess.turnRoundTime = currentTime - nextProcess.arrivalTime;
//
//            scheduledProcesses.add(nextProcess);
//            executionSteps.add(new ExecutionStep(nextProcess, currentTime - nextProcess.burstTime, currentTime));
//
//            System.out.println("Process " + nextProcess.name + " starts at time "
//                    + (currentTime - nextProcess.burstTime) + " and finishes at time " + currentTime);
//        }
//
//        printResults(scheduledProcesses);
//        displayGanttChart(executionSteps);
//    }
//
//    private void printResults(List<Process> processes) {
//        System.out.println("\nResults:");
//        int totalWaitingTime = 0;
//        int totalTurnRoundTime = 0;
//        for (Process process : processes) {
//            totalWaitingTime += process.waitingTime;
//            totalTurnRoundTime += process.turnRoundTime;
//            System.out.println("Process " + process.name + ": Waiting Time = "
//                    + process.waitingTime + ", Turnaround Time = " + process.turnRoundTime);
//        }
//        System.out.println("Average Waiting Time = " + (double) totalWaitingTime / processes.size());
//        System.out.println("Average Turnaround Time = " + (double) totalTurnRoundTime / processes.size());
//    }
//
//    private void displayGanttChart(List<ExecutionStep> steps) {
//        JFrame frame = new JFrame("Gantt Chart");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800, 200);
//
//        JPanel panel = new JPanel() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//
//                int x = 50; // Starting x position
//                int y = 50; // Vertical position of bars
//                int height = 50; // Height of each bar
//
//                for (ExecutionStep step : steps) {
//                    g.setColor(step.process.color);
//                    int width = (step.endTime - step.startTime) * 30; // Proportional to burst time
//                    g.fillRect(x, y, width, height);
//                    g.setColor(Color.BLACK);
//                    g.drawRect(x, y, width, height); // Outline for visibility
//                    g.drawString(step.process.name + " (" + step.startTime + "-" + step.endTime + ")",
//                            x + 20, y + 30);
//                    x += width;
//                }
//            }
//        };
//
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//
//    private static class ExecutionStep {
//        Process process;
//        int startTime, endTime;
//
//        public ExecutionStep(Process process, int startTime, int endTime) {
//            this.process = process;
//            this.startTime = startTime;
//            this.endTime = endTime;
//        }
//    }
//}
////package org.example;
////import java.util.ArrayList;
////import java.util.Comparator;
////import java.util.List;
////public class NonPreemptivePriorityScheduler {
////    public void schedule(List<Process> processes, int contextSwitchTime) {
////        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
////        int currentTime = 0;
////        List<Process> scheduledProcesses = new ArrayList<>();
////        while (!processes.isEmpty()) {
////            int finalCurrentTime = currentTime;
////            Process nextProcess = processes.stream()
////                    .filter(p -> p.arrivalTime <= finalCurrentTime)
////                    .min(Comparator.comparingInt(p -> p.priority))
////                    .orElse(null);
////            if (nextProcess == null) {
////                currentTime++;
////                continue;
////            }
////            processes.remove(nextProcess);
////            if (!scheduledProcesses.isEmpty()) {
////                currentTime += contextSwitchTime; // 4+2=6//9+2=11
////            }
////            nextProcess.waitingTime = currentTime - nextProcess.arrivalTime;//6-1=5//11-2=9
////            currentTime += nextProcess.burstTime;//4//6+3=9//11+2=13
////            nextProcess.turnRoundTime = currentTime - nextProcess.arrivalTime;//4//9-1=8//13-2=11
////
////            scheduledProcesses.add(nextProcess);
////            System.out.println("Process " + nextProcess.name + " start in time "
////                    + (currentTime - nextProcess.burstTime) + " and finish in time " + currentTime);
////        }
////        printResults(scheduledProcesses);
////    }
////    private void printResults(List<Process> processes) {
////        System.out.println("\n results:");
////        int totalWaitingTime = 0;
////        int totalturnRoundTime = 0;
////        for (Process process : processes) {
////            totalWaitingTime += process.waitingTime;
////            totalturnRoundTime += process.turnRoundTime;
////            System.out.println("Process " + process.name + ": Waiting Time = "
////                    + process.waitingTime + ", Turnaround Time = " + process.turnRoundTime);
////        }
////        System.out.println("average Waiting Time= " + (double) totalWaitingTime / processes.size());
////        System.out.println("average TurnRound Time = " + (double) totalturnRoundTime / processes.size());
////    }
////}
////
////
