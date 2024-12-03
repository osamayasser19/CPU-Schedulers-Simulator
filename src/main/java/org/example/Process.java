package org.example;
public class Process {
     String name;
     int arrivalTime;
    private int remainingBurstTime;
     int burstTime;
     int priority;
     int turnRoundTime;
     int waitingTime;
    private boolean startedExecution;
    private int fcaiFactor;
    private int Quantum;
    private int executedTime;


    public Process(String n, int a,int b,int p){
        name = n;
        arrivalTime = a;
        burstTime = b;
        remainingBurstTime = b;
        priority = p;
    }

    void setExecutedTime(int t){
        executedTime = t;
    }

    public int getExecutedTime(){
        return executedTime;
    }

    void setFcaiFcator(int factor){
        fcaiFactor = factor;
    };

    public int getFcaiFactor(){
        return fcaiFactor;
    };

    void setQuantum(int quantum){
        Quantum = quantum;
    }

    public int getQuantum(){
        return Quantum;
    }

    public int getBurstTime() {
        return burstTime;
    };

    public void setBurstTime(int b){
        burstTime = b;
    }

    public String getName(){
        return name;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getReamininBurstTime() {
        return remainingBurstTime;
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
