package org.example;

public class Process {
    private String name;
    private int arrivalTime;
    private int burstTime;
    private int initialBurstTime;
    private int priority;
    private int turnRoundTime;
    private int waitingTime;
    private boolean startedExecution;

    public Process(String n, int a,int b,int p){
        name = n;
        arrivalTime = a;
        initialBurstTime = b;
        burstTime = b;
        priority = p;
    }

    public int getInitialBurstTime() {
        return initialBurstTime;
    }

    public void setBurstTime(int b){
        burstTime = b;
    }

    public String getName(){
        return name;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority(){
        return priority;
    }

    public int getTurnRoundTime(){
        return turnRoundTime;
    }

    public int getWaitingTime(){
        return waitingTime;
    }

    public boolean hasStartedExecution() {
        return startedExecution;
    }
}
