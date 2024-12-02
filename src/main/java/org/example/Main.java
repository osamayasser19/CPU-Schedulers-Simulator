package org.example;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of processes: ");
        int numOfProcesses = scanner.nextInt();
        Process[] processes = new Process[numOfProcesses];

        for(int i = 0; i < numOfProcesses; i++) {
            System.out.println("Enter details of process " + i + ": ");

            scanner.nextLine();
            System.out.println("Enter name of process");
            String name = scanner.nextLine();
            System.out.println("Enter arrival time ");
            int arrivalTime = scanner.nextInt();
            System.out.println("Enter initial burst time ");
            int initialBurstTime = scanner.nextInt();
            System.out.println("Enter priority");
            int priority = scanner.nextInt();

            processes[i] = new Process(name,arrivalTime,initialBurstTime,priority);


        }
    }
}