package org.example;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class Process {
     String name;
     int arrivalTime;
    private int remainingBurstTime;
     int burstTime;
     int priority;
     int turnRoundTime;
     int waitingTime;//carry the total waiting time of a process
    private boolean startedExecution;
    private int fcaiFactor;
    private int Quantum;
    private List<Integer> wait = new ArrayList<>();//carry all the wait times of a process
    private int preemptTime;//the time when a process gets preempted
    private int oldQuantum;//to carry the old quantum before updating
    private int executedTime = 0;
    private int executedQuantum = 0;
    private int completionTime;
    Color color;


    public Process(String n, int a, int b, int p, Color color) {
        name = n;
        arrivalTime = a;
        burstTime = b;
        remainingBurstTime = b;
        priority = p;
        preemptTime = a;
        this.color = color;
    }


    ////////////////////////Setters and Getters///////////////////////////////////////

    public void setPreemptTime(int preemptTime) {
        this.preemptTime = preemptTime;
    }

    public int getPreemptTime() {
        return preemptTime;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setStartedExecution(boolean startedExecution) {
        this.startedExecution = startedExecution;
    }

    public boolean hasStartedExecution() {
        return startedExecution;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setCompletionTime(int completionTime) {

        this.completionTime = completionTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setTurnRoundTime(int turnRoundTime) {
        this.turnRoundTime = turnRoundTime;
    }

    public int getTurnRoundTime() {
        return turnRoundTime;
    }
    //--------------------------------------------------------------------------------------------------------//

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setOldQuantum(int oldQuantum) {
        this.oldQuantum = oldQuantum;
    }

    public int getOldQuantum() {
        return oldQuantum;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setExecutedQuantum(int q) {
        executedQuantum = q;
    }

    public int getExecutedQuantum() {
        return executedQuantum;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setExecutedTime(int t) {
        executedTime = t;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setFcaiFactor(int factor) {
        fcaiFactor = factor;
    }

    public int getFcaiFactor() {
        return fcaiFactor;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setQuantum(int quantum) {
        Quantum = quantum;
    }

    public int getQuantum() {
        return Quantum;
    }

    //--------------------------------------------------------------------------------------------------------//
    public void setRemainingBurstTime(int t) {
        remainingBurstTime = t;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }
    //--------------------------------------------------------------------------------------------------------//


    public int getBurstTime() {
        return burstTime;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getPriority() {
        return priority;
    }

    public List<Integer> getWaitList() {
        return wait;
    }
}
