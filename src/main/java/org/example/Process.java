package org.example;

public class Process {
    public String name;
    public int arrivalTime;
    public int remainingBurstTime;
    public int burstTime;
    public int priority;
    public int turnRoundTime;
    public int waitingTime;
    public boolean startedExecution;
    public int fcaiFactor;
    public int Quantum;
    public int executedTime;


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

    void setRemainingBurstTime(int t){
        remainingBurstTime = t;
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
