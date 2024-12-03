package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();
        System.out.println("Enter number of processes: ");
        int numOfProcesses = scanner.nextInt();
        System.out.println("Enter context switching time:");
        int contextSwitchTime = scanner.nextInt();

        for(int i = 0; i < numOfProcesses; i++) {
            System.out.println("Enter details of process " + i + ": ");

            scanner.nextLine();
            System.out.println("Enter name of process");
            String name = scanner.nextLine();
            System.out.println("Enter arrival time ");
            int arrivalTime = scanner.nextInt();
            System.out.println("Enter burst time ");
            int burstTime = scanner.nextInt();
            System.out.println("Enter priority");
            int priority = scanner.nextInt();
            System.out.println("Enter process quantum time");
            int quantumTime = scanner.nextInt();


            processes.add(new Process(name,arrivalTime,burstTime,priority));
            processes.get(i).setQuantum(quantumTime);
        }

//        FCAIScheduler fcaiScheduler = new FCAIScheduler(processes);
//        fcaiScheduler.schedule();
        NonPreemptivePriorityScheduler scheduler = new NonPreemptivePriorityScheduler();
        scheduler.schedule(processes, contextSwitchTime);
        scanner.close();

    }
}