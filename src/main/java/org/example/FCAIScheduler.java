package org.example;
import java.util.*;

import static java.lang.Math.ceil;

public class FCAIScheduler{
    List<Process> processes = new ArrayList<>();
    List <Process> readyQueue = new ArrayList<>();

    public FCAIScheduler(List Processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort processes by arrivalTime in ascending order
        this.processes = processes;
    }

    //Getting v1 (max arrival time)
    public int getV1(){
        int maxArrivalTime = 0;
        for(Process process:processes) {
            if (process.getArrivalTime() > maxArrivalTime)
                maxArrivalTime = process.getArrivalTime();
        }
        return maxArrivalTime;
    }

    //Getting v2 (max burst time)
    public int getV2(){
        int maxBurstTime = 0;
        for(Process process:processes) {
            if(process.getBurstTime() > maxBurstTime)
                maxBurstTime = process.getBurstTime();
        }
        return maxBurstTime;
    }

    //Function for calculation of fcai factor
    void calculateFcaiFactor(Process process){
        int factor = (int) ceil(((10- process.getPriority()) + (process.getArrivalTime()/(double)getV1()) + (process.getReamininBurstTime()/(double)getV2())));
        process.setFcaiFcator(factor);
    }

    //Ends the scheduling
    public boolean completeExecution(){
        boolean result = true;
        for(Process process:processes){
            if(process.getBurstTime() == 0){
                result = false;
            }
        }
        return result;
    }


    //running the process
    void runProcess(Process process,int time, int executedQuantum){
        executedQuantum++; time++;
        process.setExecutedTime(process.getExecutedTime() + executedQuantum); //getting the total executed time for the process
        process.setRemainingBurstTime(process.getBurstTime() - process.getExecutedTime()); //getting the remaining burst time for the process
    }

    void schedule() {
        int time = 0 , executedQuantum = 0 , index = 0;
        Process currentProcess = null;
        Process justExecutedProcess = null;
        boolean switchProcess = false;

        if(!processes.isEmpty()) {
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


        while(!readyQueue.isEmpty()){
        System.out.println("test awlany \n");
//            //Adding processes to ready queue depending on its arrival time
//            for(Process process:processes){
//                if(process.getArrivalTime() >= time && !readyQueue.contains(process)){
//                    readyQueue.add(process);
//                    calculateFcaiFactor(process);
//                }
//            }

            //setting current process in case of the lowest ff process is just executed
            if(justExecutedProcess == readyQueue.get(0) && readyQueue.size() > 1){
                currentProcess = readyQueue.get(1);
                index = 1;
            }
            else if(justExecutedProcess != readyQueue.get(0) && readyQueue.size() > 1){
                currentProcess = readyQueue.get(0);
                index = 0;
            }


            executedQuantum = 0; //initializing the executed amount of quantum by zero each time process enter

            while(true){
                System.out.println("test test \n");
                runProcess(currentProcess, time, executedQuantum); //running the process

                //Process are entering ready queue based on arrival time
                for(Process process:processes){
                    if(process.getArrivalTime() >= time && !readyQueue.contains(process)){
                        readyQueue.add(process);
                        calculateFcaiFactor(process);
                        readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor)); //Sort ready queue based on fcai factor each time new process arrives
                    }
                }

                if(readyQueue.get(index) != currentProcess || index != 0) //handle case if there is another process with lower fcaiFactor
                    switchProcess = true;

                //In case one of lower fcaiFactor arrived
                if((int) Math.ceil( (double)currentProcess.getQuantum() * 0.4) == executedQuantum && switchProcess ){
                    int remainingQuantum = currentProcess.getQuantum() - executedQuantum;
                    currentProcess.setQuantum(currentProcess.getQuantum() + remainingQuantum);
                    calculateFcaiFactor(currentProcess); //update fcai factor
                    readyQueue.set(index, currentProcess); //assigning the current process to its place again after changes
                    justExecutedProcess = currentProcess;
                    break;
                }
                else if(currentProcess.getQuantum() == executedQuantum && !switchProcess){
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    calculateFcaiFactor(currentProcess); //update fcai factor
                    readyQueue.set(index, currentProcess); //assigning the current process to its place again after changes
                    justExecutedProcess = currentProcess;
                    break;
                }

            }

            if(currentProcess.getReamininBurstTime() == 0) {
                System.out.println(currentProcess.getName() + " Completed \n");
                readyQueue.remove(index);
            }

            //Sort the readyQueue after each process execution
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

            //switching the flag before entering new process
            switchProcess = false;

        }

    }
}
