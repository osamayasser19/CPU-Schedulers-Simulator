package org.example;
import java.util.*;

import static java.lang.Math.ceil;

public class FCAIScheduler{
    List<Process> processes = new ArrayList<>();
    List <Process> readyQueue = new ArrayList<>();
    int counter = 0;

    public FCAIScheduler(List<Process> Processes) {
        Processes.sort(Comparator.comparingInt(Process::getArrivalTime)); // Sort processes by arrivalTime in ascending order
        this.processes = Processes;
    }

    //Getting v1 (max arrival time)
    public double getV1(){
        double maxArrivalTime = 0;
        for(Process process:processes) {
            if (process.getArrivalTime() > maxArrivalTime)
                maxArrivalTime = (double) process.getArrivalTime() /10;
        }
        return maxArrivalTime;
    }

    //Getting v2 (max burst time)
    public double getV2(){
        double maxBurstTime = 0;
        for(Process process:processes) {
            if(process.getBurstTime() > maxBurstTime)
                maxBurstTime = (double) process.getBurstTime() /10;
        }
        return maxBurstTime;
    }

    //Function for calculation of fcai factor
    void calculateFcaiFactor(Process process){
        int factor = (int) ceil(
                (10- process.getPriority())
                + ((double)process.getArrivalTime()/getV1()) +
                ((double)process.getRemainingBurstTime()/getV2())
        );
        process.setFcaiFactor(factor);
    }

    //Ends the scheduling
//    public boolean completeExecution(){
//        boolean result = true;
//        for(Process process:processes){
//            if(process.getBurstTime() == 0){
//                result = false;
//            }
//        }
//        return result;
//    }


    //running the process
    void runProcess(Process process){
//        executedQuantum++;
        process.setExecutedTime(process.getExecutedTime() + 1); //getting the total executed time for the process
        process.setRemainingBurstTime(process.getRemainingBurstTime() - 1); //getting the remaining burst time for the process
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

//        if(!processes.isEmpty())
//            currentProcess = processes.get(0);


//        currentProcess = new Process("Process 1" , 0 ,17 , 4);
//        currentProcess.setQuantum(4);
//        time = currentProcess.getArrivalTime();
//        readyQueue.add(currentProcess);
//
//        Process newProcess = new Process("Process 2" , 3 ,4 , 2);
//        currentProcess.setQuantum(4);
//        readyQueue.add(newProcess);

        while(!readyQueue.isEmpty()) {
            System.out.println("test awlany \n");

            //setting current process in case of the lowest ff process is just executed
            if (readyQueue.size() > 1) {
                if (justExecutedProcess == readyQueue.get(0)) {
                    currentProcess = readyQueue.get(1);
                    index = 1;
                } else if (justExecutedProcess != readyQueue.get(0)) {
                    currentProcess = readyQueue.get(0);
                    index = 0;
                }
                else if(!readyQueue.contains(currentProcess)){
                    currentProcess = readyQueue.get(0);
                    index = 0;
                }
            }
            else{
                currentProcess = readyQueue.get(0);
                index = 0;
            }


//            assert currentProcess != null;
//            currentProcess.setExecutedQuantum(0);

            while(true){
                readyQueue.remove(currentProcess); //removing current process from the ready queue before updating its components
                System.out.println("nth cycle \n");

                //running Process

                runProcess(currentProcess);
                currentProcess.setExecutedQuantum(currentProcess.getExecutedQuantum() + 1);
                time++;

                calculateFcaiFactor(currentProcess);
                readyQueue.add(currentProcess);
                readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor)); //adding the currentProcess after updating


                //Process are entering ready queue based on arrival time
                for(Process process:processes){
                    if(process.getArrivalTime() == time && !readyQueue.contains(process)){
                        calculateFcaiFactor(process);
                        readyQueue.add(process);
                        readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor)); //Sort ready queue based on fcai factor each time new process arrives
                    }
                }

                System.out.println(readyQueue.get(0).getName());
                System.out.println("\n"+ "Time is "+ time);
                //Handle case if there is another process with lower fcaiFactor
                if(readyQueue.get(index) != currentProcess || index != 0) {
                    switchProcess = true;
                }

                System.out.println("switchProcesStatus equal : " + switchProcess);

                //In case one of lower fcaiFactor arrived
                int preempt = (int) ceil(currentProcess.getQuantum() * 0.4);
//                System.out.println(preempt);
                if(preempt <= currentProcess.getExecutedQuantum() && switchProcess){

                    System.out.println(currentProcess.getName() + " executed for " + currentProcess.getExecutedQuantum() + " seconds and preempted");


                    //removing the old instance from the queue
                    readyQueue.remove(currentProcess);

                    //Updating the quantum of the currentProcess
                    int remainingQuantum = currentProcess.getQuantum() - currentProcess.getExecutedQuantum();
                    currentProcess.setQuantum(currentProcess.getQuantum() + remainingQuantum);

                    //Updating Fcai factor
                    calculateFcaiFactor(currentProcess);

                    //re-Iinitializing the quantum of zero
                    currentProcess.setExecutedQuantum(0); //re-initializing the executed quantum to 0 after finishing

                    //Adding currentProcess to the ready Queue after updating
                    readyQueue.add(currentProcess);

                    //Sorting
                    readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

                    justExecutedProcess = currentProcess;

                    break;
                }
                else if(currentProcess.getQuantum() == currentProcess.getExecutedQuantum() && !switchProcess){

                    System.out.println(currentProcess.getName() + " executed for " + currentProcess.getExecutedQuantum() + " seconds");

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

                    //Sorting
                    readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

                    justExecutedProcess = currentProcess;

                    break;
                }
                else if(currentProcess.getRemainingBurstTime() == 0){
                    break;
                }
//                System.out.println(currentProcess.getExecutedQuantum());
//                System.out.println("V1 is : "+getV1());
//                System.out.println("v2 is: " + getV2());
//                System.out.println(currentProcess.getFcaiFactor());
//                System.out.println(time);
//                System.out.print(currentProcess.getReamininBurstTime());
//                System.out.print("\n" + switchProcess + "\n");
            }

//            //Process Completed
//            if(currentProcess.getRemainingBurstTime() == 0) {
//                readyQueue.remove(currentProcess);
//                justExecutedProcess = null;
//                System.out.println(currentProcess.getName() + " Completed \n");
//            }

            if(currentProcess.getRemainingBurstTime() == 0) {
                readyQueue.remove(currentProcess);
                justExecutedProcess = null;
                System.out.println(currentProcess.getName() + " Completed \n");
                for(Process process:readyQueue)
                    System.out.println("B2olk ehhhh " + process.getName());
            }

//            System.out.println(readyQueue.get(0).getName());

            //Sort the readyQueue after each process execution
            readyQueue.sort(Comparator.comparingInt(Process::getFcaiFactor));

            //switching the flag before entering new process
            switchProcess = false;
//            counter++;
////            System.out.println(currentProcess.getRemainingBurstTime());
//            if(counter == 10)
//                break;

        }

    }
}
