//package org.example;
//public class Process {
//    String name;
//    int arrivalTime;
//    public int remainingBurstTime;
//    public int burstTime;
//    public int priority;
//    public int turnRoundTime;
//    public int waitingTime;
//    public boolean startedExecution= false;
//    private int fcaiFactor;
//    private int Quantum;
//    private int executedTime;
//    private int completionTime;
//    public Process(String n, int a, int b, int p) {
//        name = n;
//        arrivalTime = a;
//        burstTime = b;
//        remainingBurstTime = b;
//        priority = p;
//    }
//
//    void setExecutedTime(int t) {
//        executedTime = t;
//    }
//
//    public int getExecutedTime() {
//        return executedTime;
//    }
//
//    void setFcaiFcator(int factor) {
//        fcaiFactor = factor;
//    }
//
//
//
//    public int getFcaiFactor() {
//        return fcaiFactor;
//    }
//
//
//
//    void setQuantum(int quantum) {
//        Quantum = quantum;
//    }
//
//    public int getQuantum() {
//        return Quantum;
//    }
//
//    public int getBurstTime() {
//        return burstTime;
//    }
//
//    public void setTurnRoundTime(int turnRoundTime) {
//        this.turnRoundTime = turnRoundTime;
//    }
//
//    public void setWaitingTime(int waitingTime) {
//        this.waitingTime = waitingTime;
//    }
//
//    public void setBurstTime(int b) {
//        burstTime = b;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getArrivalTime() {
//        return arrivalTime;
//    }
//
//    public int getReamininBurstTime() {
//        return remainingBurstTime;
//    }
//
//    void setRemainingBurstTime(int time){
//        remainingBurstTime = time;
//    }
//
//
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public int getTurnRoundTime() {
//        return turnRoundTime;
//    }
//
//    public int getWaitingTime() {
//        return waitingTime;
//    }
//
//    // New method to set the completion time
//    public void setCompletionTime(int time) {
//        this.completionTime = time;
//    }
//    // New method to get the completion time
//    public int getCompletionTime() {
//        return completionTime;
//    }
//    // New method to mark that the process has started execution
//    public void setStartedExecution(boolean started) {
//        this.startedExecution = started;
//    }
//    public boolean hasStartedExecution() {
//        return startedExecution;
//    }
//}


package org.example;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
public class Process {
    public String name;
    public int arrivalTime;
    public int remainingBurstTime;
    public int burstTime;
    public int priority;
    public int turnRoundTime;
    public int waitingTime;//carry the total waiting time of a process
    public boolean startedExecution;
    public int fcaiFactor;
    public int Quantum;
    private List<Integer> wait = new ArrayList<>();//carry all the wait times of a process
    private int preemptTime;//the time when a process gets preempted
    public int oldQuantum;//to carry the old quantum before updating
    public int executedTime = 0;
    public int executedQuantum = 0;
    public int completionTime;
    Color color;
    public int getPreemptTime() {
        return preemptTime;
    }

    public void setPreemptTime(int preemptTime) {
        this.preemptTime = preemptTime;
    }


    public void setTurnRoundTime(int turnRoundTime) {
        this.turnRoundTime = turnRoundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }


    public int getOldQuantum() {
        return oldQuantum;
    }

    public void setOldQuantum(int oldQuantum) {
        this.oldQuantum = oldQuantum;
    }


    public Process(String n, int a, int b, int p,Color color) {
        name = n;
        arrivalTime = a;
        burstTime = b;
        remainingBurstTime = b;
        priority = p;
        preemptTime = a;
        this.color=color;
    }

    public int getExecutedQuantum() {
        return executedQuantum;
    }

    void setExecutedQuantum(int q) {
        executedQuantum = q;
    }

    void setExecutedTime(int t) {
        executedTime = t;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    void setFcaiFactor(int factor) {
        fcaiFactor = factor;
    }

    ;

    public int getFcaiFactor() {
        return fcaiFactor;
    }

    ;

    void setQuantum(int quantum) {
        Quantum = quantum;
    }

    public int getQuantum() {
        return Quantum;
    }

    public int getBurstTime() {
        return burstTime;
    }

    ;

    public void setBurstTime(int b) {
        burstTime = b;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    void setRemainingBurstTime(int t) {
        remainingBurstTime = t;
    }

    public int getPriority() {
        return priority;
    }

    public int getTurnRoundTime() {
        return turnRoundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public boolean hasStartedExecution() {
        return startedExecution;
    }

    public List<Integer> getWait() {
        return wait;
    }

    public void setWait(List<Integer> wait) {
        this.wait = wait;
    }
}
