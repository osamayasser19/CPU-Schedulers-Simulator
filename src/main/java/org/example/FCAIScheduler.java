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
        return maxArrivalTime / 10;
    }

    //Getting v2 (max burst time)
    public double getV2() {
        double maxBurstTime = 0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurstTime) {
                maxBurstTime = ((double) process.getBurstTime());
            }
        }
        return maxBurstTime / 10;
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
        process.setExecutedTime(process.getExecutedTime() + 1); //getting the total executed time for the process
        process.setRemainingBurstTime(process.getRemainingBurstTime() - 1); //getting the remaining burst time for the process
    }

    void schedule() {
        int time = 0, executedQuantum = 0, index = 0, oldTime = 0;
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
            currentProcess.getWaitList().add(time - currentProcess.getPreemptTime());//add to the wait list the time of the waiting of the process from the last time it executed

            System.out.println("Process " + currentProcess.getName() + " started execution at " + time);


            if (executionOrder.isEmpty()) {
                executionOrder.add(currentProcess.getName());
            } else {
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
                readyQueue.add(currentProcess); //adding the currentProcess after updating
                readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

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
                    System.out.println(currentProcess.getName() + " Updated Quantum is " + currentProcess.getOldQuantum() + " -> " + currentProcess.getQuantum());
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
                    System.out.println(currentProcess.getName() + " Updated Quantum is " + currentProcess.getOldQuantum() + " -> " + currentProcess.getQuantum());
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
                System.out.println(currentProcess.getName() + " Completed at " + time + "\n");
            }

            //Sort the readyQueue after each process execution
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

            //switching the flag before entering new process
            switchProcess = false;
        }


        printWT_TT();
        printAVG();

        for (String string : executionOrder) {
            System.out.print(string + "->");
        }
        visualizeExecutionOrder(executionOrder); // Visualize the execution order
    }

    public void printWT_TT() {
        System.out.println("-----------------------------------------");
        int total = 0;
        for (Process process : processes) {
            for (int waitTime : process.getWaitList()) {
                total += waitTime;
            }
            process.setWaitingTime(total);
            System.out.println(process.getName() + " Wait Time: " + total + " Turnaround Time: " + process.getTurnRoundTime());
            total = 0;
        }
    }

    public void printAVG() {
        System.out.println("-----------------------------------------");
        int totalWaitTime = 0;
        int totalTurnaroundTime = 0;
        for (Process process : processes) {
            totalWaitTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnRoundTime();
        }
        System.out.println("Average Wait Time: " + (totalWaitTime / processes.size()) + "\nAverage Turnaround Time: " + (totalTurnaroundTime / processes.size()));
    }

    private Process findProcessByName(String name) {
        for (Process process : processes) {
            if (process.getName().equals(name)) {
                return process; // Return the process if it matches the name
            }
        }
        return null; // Should never happen if the process exists
    }

    private void visualizeExecutionOrder(List<String> executionOrder) {
        JFrame frame = new JFrame("Fcai Scheduler Execution Order");        // create the main window to display the execution order and process details
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the application when the window is closed
        frame.setSize(800, 600); // set the window size


        JPanel panel = new JPanel();//create the panel that hold the components
        panel.setLayout(new BorderLayout()); // use BorderLayout to add multiple components

        JPanel colorPanel = new JPanel();//create a color panel that show each process color
        colorPanel.setLayout(new FlowLayout()); // make the color panel arranged horizontally


        for (Process process : processes) {//loop over each process and make a color box
            JLabel colorLabel = new JLabel();//creating a new label
            colorLabel.setBackground(process.color);//setting the color of the label to the color of the process
            colorLabel.setOpaque(true);
            colorLabel.setPreferredSize(new Dimension(50, 30));  //sizing the labels
            colorPanel.add(colorLabel);//adding the box to the color panel component
        }


        panel.add(colorPanel, BorderLayout.NORTH);//adding the colorPanel to the north side of the main panel

        JPanel executionPanel = new JPanel() {//creating an execution panel to visualize the execution order of the processes
            @Override
            protected void paintComponent(Graphics g) {//overriding the paintComponent method to adjust it to our execution order
                super.paintComponent(g); // call the main method
                int xPosition = 50; // Initial position for drawing the processes on the horizontal axis
                int scaleFactor = 10;  // a factor for scaling the rectangles of each process
                int spacing = 20;  // space between each process


                for (String processName : executionOrder) {//loop on the execution order list
                    Process process = findProcessByName(processName); // find the process based on its name
                    g.setColor(process.color); // Set the color associated with the process
                    int width = process.getBurstTime() * scaleFactor; //calculate the width of the box according to the burst time
                    g.fillRect(xPosition, 50, width, 50); // drawing a filled rectangle representing the process
                    g.setColor(Color.BLACK); // set the color to black for the border and text
                    g.drawRect(xPosition, 50, width, 50); // drawing the border of the rectangle
                    g.drawString(processName, xPosition + 10, 80); // Draw the process name inside the rectangle
                    xPosition += width + spacing;  // updating the starting position for the next process
                }
            }
        };


        panel.add(executionPanel, BorderLayout.CENTER); // adding the execution panel to the main panel in the center

        String[] columnNames = {"Name", "Arrival", "Burst", "Waiting", "Turnaround", "Color"};//adding the name of each column to an array of strings
        Object[][] data = new Object[processes.size()][7];//making a 2d array to fill with the data with width of 7 and hieght according to the processes size

        for (int i = 0; i < processes.size(); i++) {//filling each row with process data
            Process process = processes.get(i);
            data[i][0] = process.getName();
            data[i][1] = process.getArrivalTime();
            data[i][2] = process.getBurstTime();
            data[i][3] = process.getWaitingTime();
            data[i][4] = process.getTurnRoundTime();
            data[i][5] = process.getCompletionTime();
            data[i][6] = new JLabel(" ", JLabel.CENTER);  //creating an empty label to display color
            ((JLabel) data[i][6]).setBackground(process.color); // set the color to the process color
            ((JLabel) data[i][6]).setOpaque(true);
        }


        JTable table = new JTable(data, columnNames);//making a table and adding the data and the column names to it
        table.setPreferredScrollableViewportSize(new Dimension(750, 300)); // set the size of the table


        //set the width of each column
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);

        JScrollPane tableScrollPane = new JScrollPane(table);//add the table for a scroll panel

        panel.add(tableScrollPane, BorderLayout.SOUTH);//adding the table to the main panel in the bottom


        frame.add(panel);//adding the main panel to the frame
        frame.setVisible(true);//making the frame visible
    }

}

