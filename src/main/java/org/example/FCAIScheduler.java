package org.example;

import java.util.*;

import static java.lang.Math.ceil;

public class FCAIScheduler {
    List<Process> processes = new ArrayList<>();
    List<Process> readyQueue = new ArrayList<>();

    public FCAIScheduler(List Processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort processes by arrivalTime in ascending order
        this.processes = Processes;
    }

    //Getting v1 (max arrival time)
    public double getV1() {
        int maxArrivalTime = 0;
        for (Process process : processes) {
            if (process.getArrivalTime() > maxArrivalTime) {
                maxArrivalTime = process.getArrivalTime();
            }
        }
        return (maxArrivalTime / 10.0);

    }

    //Getting v2 (max burst time)
    public double getV2() {
        int maxBurstTime = 0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBurstTime)
                maxBurstTime = process.getBurstTime();
        }
        return (maxBurstTime / 10.0);
    }

    //Function for calculation of fcai factor
    void calculateFcaiFactor(Process process) {
        int factor = (int) ceil(((10 - process.getPriority()) + (process.getArrivalTime() / getV1()) + (process.getRemainingBurstTime() / getV2())));
        process.setFcaiFcator(factor);
    }

    //Ends the scheduling
    public boolean completeExecution() {
        boolean result = true;
        for (Process process : processes) {
            if (process.getBurstTime() == 0) {
                result = false;
            }
        }
        return result;
    }

    public int TotalProcessesTime() {//get the total time that the processes would take to finish
        int total = 0;
        for (Process process : processes) {
            total += process.getBurstTime();
        }
        return total;
    }

    public int getMinFactor() {//get the minimum FCAI Factor
        int index = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.get(i).getFcaiFactor() < min) {
                min = readyQueue.get(i).getFcaiFactor();
                index = i;
            }
        }
        return index;
    }

    void AdjustReadyQueue(int time) {//add to the ready queue the arrived processes
        for (Process process : processes) {
            if (process.getArrivalTime() == time)
                readyQueue.add(process);

            if (process.getRemainingBurstTime() == 0)
                readyQueue.remove(process);
        }
    }

    //running the process
    void runProcess(Process process, int executedQuantum) {
        executedQuantum++;

        process.setExecutedTime(process.getExecutedTime() + executedQuantum); //getting the total executed time for the process
        process.setRemainingBurstTime(process.getBurstTime() - process.getExecutedTime()); //getting the remaining burst time for the process
    }

    void schedule() {
        int time = 0, executedQuantum = 0, index = 0;
        Process currentProcess = null;
        Process justExecutedProcess = null;
        boolean switchProcess = false;

        if (!processes.isEmpty()) {
            time = processes.get(0).getArrivalTime();
            currentProcess = processes.get(0); //starting the readyQueue with the first arrival one
            readyQueue.add(currentProcess);
        }


//        currentProcess = new Process("Process 1" , 0 ,17 , 4);
//        currentProcess.setQuantum(4);
//        time = currentProcess.getArrivalTime();
//        readyQueue.add(currentProcess);
//
//        Process newProcess = new Process("Process 2" , 3 ,4 , 2);
//        currentProcess.setQuantum(4);
//        readyQueue.add(newProcess);


        while (!readyQueue.isEmpty()) {
            System.out.println("test awlany \n");
//            //Adding processes to ready queue depending on its arrival time
//            for(Process process:processes){
//                if(process.getArrivalTime() >= time && !readyQueue.contains(process)){
//                    readyQueue.add(process);
//                    calculateFcaiFactor(process);
//                }
//            }

            //setting current process in case of the lowest ff process is just executed
            if (justExecutedProcess == readyQueue.get(0) && readyQueue.size() > 1) {
                currentProcess = readyQueue.get(1);
                index = 1;
            } else if (justExecutedProcess != readyQueue.get(0) && readyQueue.size() > 1) {
                currentProcess = readyQueue.get(0);
                index = 0;
            }


            executedQuantum = 0; //initializing the executed amount of quantum by zero each time process enter

            while (true) {
                System.out.println("test test \n");
                runProcess(currentProcess, executedQuantum); //running the process

                //Process are entering ready queue based on arrival time
                for (Process process : processes) {
                    if (process.getArrivalTime() >= time && !readyQueue.contains(process)) {
                        readyQueue.add(process);
                        calculateFcaiFactor(process);
                        readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor)); //Sort ready queue based on fcai factor each time new process arrives
                    }
                }

                if (readyQueue.get(index) != currentProcess || index != 0) //handle case if there is another process with lower fcaiFactor
                    switchProcess = true;

                //In case one of lower fcaiFactor arrived
                if ((int) Math.ceil((double) currentProcess.getQuantum() * 0.4) == executedQuantum && switchProcess) {
                    int remainingQuantum = currentProcess.getQuantum() - executedQuantum;
                    currentProcess.setQuantum(currentProcess.getQuantum() + remainingQuantum);
                    calculateFcaiFactor(currentProcess); //update fcai factor
                    readyQueue.set(index, currentProcess); //assigning the current process to its place again after changes
                    justExecutedProcess = currentProcess;
                    break;
                } else if (currentProcess.getQuantum() == executedQuantum && !switchProcess) {
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    calculateFcaiFactor(currentProcess); //update fcai factor
                    readyQueue.set(index, currentProcess); //assigning the current process to its place again after changes
                    justExecutedProcess = currentProcess;
                    break;
                }

            }

            if (currentProcess.getRemainingBurstTime() == 0) {
                System.out.println(currentProcess.getName() + " Completed \n");
                readyQueue.remove(index);
            }

            //Sort the readyQueue after each process execution
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

            //switching the flag before entering new process
            switchProcess = false;

        }

    }

    void calcFactors(List<Process> Processes) {//function to calculate the FCAI Factor of each Process
        for (Process process : Processes) {
            calculateFcaiFactor(process);
        }
    }

    void scheduler() {
        int time = 0;
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            System.out.println("hello1");
            AdjustReadyQueue(time);//add the arrived processes to the ready queue or remove a process when its finished
            calcFactors(readyQueue);//calculate the Factors
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));//sort the ready queue to get the least Fcai Factor which is at index 0

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.getFirst();//get the current process we will work on which is the first one in the ready queue
                int quantum = currentProcess.getQuantum();//get the quantum of the current process
                int executionTime = (int) ceil(0.4 * quantum);//calculate the 0.4 of the quantum
                int executedTime = 0;

                while (executedTime < executionTime) {
                    System.out.println("hello1");

                    runProcess(currentProcess, executedTime);//run the process
                    executedTime++;//update the executedTime
                    time++;//update the currentTime

                    AdjustReadyQueue(time);//adjust the readyQueue to the new time
                    calcFactors(readyQueue);//calculate the Factors after adjusting the ready queue to see if there is smaller factor
                    readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));//sorting the ready queue after updating the factors
                    if (readyQueue.getFirst() != currentProcess) {//check if the first process is still equals the current process which means it still has the least factor
                        break;
                    }
                }
                if (executedTime == executionTime && currentProcess.getRemainingBurstTime() > 0) {//check if the process finished or not to update the quantum according to it
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);//adding 2 to the quantum because the process used its quantum but still has remaining work
                } else if (currentProcess.getRemainingBurstTime() > 0) {//check if the process got preemted without finishing its quantum
                    int remainingQuantum = executionTime - executedTime;//calculating the remaining quantum
                    currentProcess.setQuantum(currentProcess.getQuantum() + remainingQuantum);//adding the remaining quantum to the original quantum
                }

                if (currentProcess.getRemainingBurstTime() == 0) {//if the process remaining burst time is zero we remove it
                    readyQueue.remove(currentProcess);//removing from the ready queue
                    processes.remove(currentProcess);//removing from the process queue
                    System.out.println(currentProcess.getName() + " Completed \n");//print a complete message
                }


            } else {//update the time if no process in the ready queue
                time++;
            }

        }
    }
}
