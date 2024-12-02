package org.example;

import java.util.Arrays;

import static java.lang.Math.ceil;

public class FCAIScheduler{
    private Process[] processes;

    public FCAIScheduler(Process[] processes) {
        // Sort processes by arrivalTime in ascending order
        Arrays.sort(processes, (p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
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
    public int calculateFcaiFactor(Process process){
        int factor = (int) Math.ceil(((10- process.getPriority()) + (process.getArrivalTime()/(double)getV1()) + (process.getReamininBurstTime()/(double)getV2())));
        return factor;
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

    void schedule() {

    }

    
}
