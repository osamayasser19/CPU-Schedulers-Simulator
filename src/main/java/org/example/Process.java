package org.example;
public class Process {
    String name;
    int arrivalTime;
    public int remainingBurstTime;
    int burstTime;
    int priority;
    int turnRoundTime;
    int waitingTime;
    public boolean startedExecution= false;
    private int fcaiFactor;
    private int Quantum;
    private int executedTime=-1;
    int completionTime;
    public Process(String n, int a, int b, int p) {
        name = n;
        arrivalTime = a;
        burstTime = b;
        remainingBurstTime = b;
        priority = p;
    }

    void setExecutedTime(int t) {
        executedTime = t;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    void setFcaiFcator(int factor) {
        fcaiFactor = factor;
    }


    public int getFcaiFactor() {
        return fcaiFactor;
    }


    void setQuantum(int quantum) {
        Quantum = quantum;
    }

    public int getQuantum() {
        return Quantum;
    }

    public int getBurstTime() {
        return burstTime;
    }
    public void setTurnRoundTime(int turnRoundTime) {
        this.turnRoundTime = turnRoundTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

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

    public int getPriority() {
        return priority;
    }

    public int getTurnRoundTime() {
        return turnRoundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    // New method to set the completion time
    public void setCompletionTime(int time) {
        this.completionTime = time;
    }
    // New method to get the completion time
    public int getCompletionTime() {
        return completionTime;
    }
    // New method to mark that the process has started execution
    public void setStartedExecution(boolean started) {
        this.startedExecution = started;
    }
    public boolean hasStartedExecution() {
        return startedExecution;
    }
    public void setRemainingBurstTime(int time) {
        remainingBurstTime = time;
    }
}
