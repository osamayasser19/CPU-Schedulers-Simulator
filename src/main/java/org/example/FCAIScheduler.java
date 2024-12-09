package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.Math.ceil;

public class FCAIScheduler {
    List<Process> processes = new ArrayList<>();
    List<Process> readyQueue = new ArrayList<>();
    List<String> executionOrder = new ArrayList<>();
    int counter = 0;

    public FCAIScheduler(List<Process> Processes) {
        Processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort processes by arrivalTime in ascending order
        this.processes = Processes;
    }

    //Getting v1 (max arrival time)
    public double getV1() {
        double maxArrivalTime = 0;
        for (Process process : processes) {
            if (process.getArrivalTime() > maxArrivalTime)
                maxArrivalTime = (double) process.getArrivalTime();
        }
        return maxArrivalTime/10;
    }

    //Getting v2 (max burst time)
    public double getV2() {
        double maxBurstTime = 0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurstTime) {
                maxBurstTime = ( (double) process.getBurstTime());
            }
        }
        return maxBurstTime/10;
    }

    //Function for calculation of fcai factor
    void calculateFcaiFactor(Process process) {
        int factor = (int) ceil(
                (10 - process.getPriority())
                        + ceil((double) process.getArrivalTime() / getV1()) +
                        ceil((double) process.getRemainingBurstTime() / getV2())
        );
        process.setFcaiFactor(factor);
    }



    //running the process
    void runProcess(Process process) {
//        executedQuantum++;
        process.setExecutedTime(process.getExecutedTime() + 1); //getting the total executed time for the process
        process.setRemainingBurstTime(process.getRemainingBurstTime() - 1); //getting the remaining burst time for the process
    }

    void schedule() {
        int time = 0, executedQuantum = 0, index = 0 , oldTime = 0;
        Process currentProcess = null;
        Process justExecutedProcess = null;
        boolean switchProcess = false;

        if (!processes.isEmpty()) {
            time = processes.getFirst().getArrivalTime();
            currentProcess = processes.getFirst(); //starting the readyQueue with the first arrival one
            readyQueue.add(currentProcess);
        }

        while (!readyQueue.isEmpty()) {

            //setting current process in case of the lowest ff process is just executed
            if (readyQueue.size() > 1) {
                if (justExecutedProcess == readyQueue.get(0)) {
                    currentProcess = readyQueue.get(1);
                    index = 1;
                } else if (justExecutedProcess != readyQueue.get(0)) {
                    currentProcess = readyQueue.getFirst();
                    index = 0;
                } else if (!readyQueue.contains(currentProcess)) {
                    currentProcess = readyQueue.getFirst();
                    index = 0;
                }
            } else {
                currentProcess = readyQueue.getFirst();
                index = 0;
            }

            assert currentProcess != null;
            currentProcess.getWait().add(time - currentProcess.getPreemptTime());

            System.out.println("Process "+ currentProcess.getName() + " started execution at " + time);


            if(executionOrder.isEmpty()){
                executionOrder.add(currentProcess.getName());
            }
            else {
                if (executionOrder.getLast() != currentProcess.getName()) {
                    executionOrder.add(currentProcess.getName());
                }
            }

            while (true) {
                readyQueue.remove(currentProcess); //removing current process from the ready queue before updating its components


                //running Process
                runProcess(currentProcess);
                currentProcess.setExecutedQuantum(currentProcess.getExecutedQuantum() + 1);
                time++;

                calculateFcaiFactor(currentProcess);
                readyQueue.add(currentProcess);
                readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor)); //adding the currentProcess after updating

                //Process are entering ready queue based on arrival time
                for (Process process : processes) {
                    if (process.getArrivalTime() == time && !readyQueue.contains(process)) {
                        calculateFcaiFactor(process);
                        readyQueue.add(process);
                        readyQueue.sort(
                                Comparator.comparingInt(Process::getFcaiFactor) // Primary sorting by FCAI Factor
                        );
                    }
                }



                //Handle case if there is another process with lower fcaiFactor
                if (readyQueue.get(index) != currentProcess || index != 0) {
                    switchProcess = true;
                }


                //In case one of lower fcaiFactor arrived
                int preempt = (int) ceil(currentProcess.getQuantum() * 0.4);
//                System.out.println(preempt);
                if (preempt <= currentProcess.getExecutedQuantum() && switchProcess) {

                    System.out.println(currentProcess.getName() + " executed for " + currentProcess.getExecutedQuantum() + " seconds and preempted at " + time);
                    //removing the old instance from the queue
                    readyQueue.remove(currentProcess);

                    //Updating the quantum of the currentProcess
                    int remainingQuantum = currentProcess.getQuantum() - currentProcess.getExecutedQuantum();
                    currentProcess.setQuantum(currentProcess.getQuantum() + remainingQuantum);
                    //Updating Fcai factor
                    calculateFcaiFactor(currentProcess);

                    //re-Initializing the quantum of zero
                    currentProcess.setExecutedQuantum(0); //re-initializing the executed quantum to 0 after finishing

                    //Adding currentProcess to the ready Queue after updating
                    readyQueue.add(currentProcess);
                    System.out.println(currentProcess.getName() + " Updated Quantum is " + currentProcess.getOldQuantum() + "->" + currentProcess.getQuantum());
                    System.out.println("---------------------------------------------\n");
                    currentProcess.setOldQuantum(currentProcess.getQuantum());
                    currentProcess.setPreemptTime(time);

                    //Sorting
                    readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

                    justExecutedProcess = currentProcess;

                    break;
                } else if (currentProcess.getQuantum() == currentProcess.getExecutedQuantum() && !switchProcess) {

                    System.out.println(currentProcess.getName() + " executed for " + currentProcess.getExecutedQuantum() + " seconds and exits at " + time);
                    //removing the old instance from the queue
                    readyQueue.remove(currentProcess);

                    //Updating Quantum
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);

                    //Updating Fcai Factor
                    calculateFcaiFactor(currentProcess);

                    //re-Initialzing the executed quantum to 0 after finishing
                    currentProcess.setExecutedQuantum(0);

                    //Adding the process to the queue again after updating
                    readyQueue.add(currentProcess);
                    System.out.println(currentProcess.getName() + " Updated Quantum is " + currentProcess.getOldQuantum() + "->" + currentProcess.getQuantum());
                    System.out.println("---------------------------------------------\n");

                    currentProcess.setOldQuantum(currentProcess.getQuantum());
                    currentProcess.setPreemptTime(time);
                    //Sorting
                    readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

                    justExecutedProcess = currentProcess;

                    break;
                } else if (currentProcess.getRemainingBurstTime() == 0) {
                    break;
                }
            }


            if (currentProcess.getRemainingBurstTime() == 0) {
                currentProcess.setTurnRoundTime(time - currentProcess.getArrivalTime());
                readyQueue.remove(currentProcess);
                justExecutedProcess = null;
                System.out.println(currentProcess.getName() + " Completed at" + time+ "\n");
            }

            //Sort the readyQueue after each process execution
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

            //switching the flag before entering new process
            switchProcess = false;
        }


        printWT_TT();
        printAVG();

        for(String string:executionOrder){
            System.out.print(string + "->");
        }
        visualizeExecutionOrder(executionOrder); // Visualize the execution order
    }

    public void printWT_TT() {
        int total = 0;
        for (Process process : processes) {
            for (int waitTime : process.getWait()) {
                total += waitTime;
            }
            process.setWaitingTime(total);
            System.out.println(process.getName() + " wait Time: " + total + " Turnaround Time: " + process.getTurnRoundTime());
            total = 0;
        }
    }

    public void printAVG() {
        System.out.println("-----------------------------------------");
        int avgWaitTime = 0;
        int avgTurnaroundTime = 0;
        for (Process process : processes) {
            avgWaitTime += process.getWaitingTime();
            avgTurnaroundTime += process.getTurnRoundTime();
        }
        System.out.println("Average Wait Time: " + (avgWaitTime / processes.size()) + "\nTurnaround Time: " + (avgTurnaroundTime / processes.size()));
    }

    private Process findProcessByName(String name) {
        for (Process process : processes) {
            if (process.name.equals(name)) {
                return process; // Return the process if it matches the name
            }
        }
        return null; // Should never happen if the process exists
    }


    private void visualizeExecutionOrder(List<String> executionOrder) {
        // Create a new JFrame to display the execution order and process details
        JFrame frame = new JFrame("Fcai Scheduler Execution Order");
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
        String[] columnNames = {"Name", "Arrival", "Burst", "Waiting", "Turnaround", "Color"};
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

        // Add the table to a scroll pane for better viewing
        JScrollPane tableScrollPane = new JScrollPane(table);

        // Add the table scroll pane to the panel in the south section (below the graph)
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add the panel to the frame and make the frame visible
        frame.add(panel);
        frame.setVisible(true);
    }

}

