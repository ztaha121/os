import java.util.*;

public class Project {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select Scheduling Algorithm:");
            System.out.println("1. FCFS");
            System.out.println("2. SJF (Non-preemptive)");
            System.out.println("3. SJF (Preemptive)");
            System.out.println("4. Round Robin");
            System.out.println("5. Banker's Algorithm");
            System.out.println("6. FIFO Page Replacement");
            System.out.println("7. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    fcfs(scanner);
                    break;
                case 2:
                    sjfNonPreemptive(scanner);
                    break;
                case 3:
                    sjfPreemptive(scanner);
                    break;
                case 4:
                    roundRobin(scanner);
                    break;
                case 5:
                    bankersAlgorithm(scanner);
                    break;
                case 6:
                    fifoPageReplacement(scanner);
                    break;
                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // FCFS Scheduling
    public static void fcfs(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        int[] arrivalTimes = new int[n];
        int[] burstTimes = new int[n];

        System.out.println("Enter Arrival Times:");
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = scanner.nextInt();
        }

        System.out.println("Enter Burst Times:");
        for (int i = 0; i < n; i++) {
            burstTimes[i] = scanner.nextInt();
        }

        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int totalWaitingTime = 0, totalTurnaroundTime = 0;

        waitingTime[0] = 0; // First process has no waiting time
        for (int i = 1; i < n; i++) {
            waitingTime[i] = waitingTime[i - 1] + burstTimes[i - 1];
            totalWaitingTime += waitingTime[i];
        }

        for (int i = 0; i < n; i++) {
            turnaroundTime[i] = waitingTime[i] + burstTimes[i];
            totalTurnaroundTime += turnaroundTime[i];
        }

        System.out.println("\nFCFS Scheduling Results:");
        System.out.printf("%-10s%-15s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time", " Completion Time");
        int completionTime = 0;
        for (int i = 0; i < n; i++) {
            completionTime += burstTimes[i];
            System.out.printf("P%d\t\t%d\t\t%d\t\t%d\n", i + 1, waitingTime[i], turnaroundTime[i], completionTime);
        }
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / n);

        // Gantt Chart
        System.out.println("Gantt Chart:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + "\t");
        }
        System.out.println();
        int currentTime = 0;
        for (int i = 0; i < n; i++) {
            currentTime += burstTimes[i];
        }
        System.out.println("0\t" + currentTime);
    }

    // SJF Non-preemptive Scheduling
    public static void sjfNonPreemptive(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        int[] arrivalTimes = new int[n];
        int[] burstTimes = new int[n];

        System.out.println("Enter Arrival Times:");
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = scanner.nextInt();
        }

        System.out.println("Enter Burst Times:");
        for (int i = 0; i < n; i++) {
            burstTimes[i] = scanner.nextInt();
        }

        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        boolean[] completed = new boolean[n];
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        int currentTime = 0;
        int completedProcesses = 0;

        while (completedProcesses < n) {
            int idx = -1;
            int minBurst = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (!completed[i] && arrivalTimes[i] <= currentTime && burstTimes[i] < minBurst) {
                    minBurst = burstTimes[i];
                    idx = i;
                }
            }

            if (idx != -1) {
                waitingTime[idx] = currentTime - arrivalTimes[idx];
                currentTime += burstTimes[idx];
                turnaroundTime[idx] = waitingTime[idx] + burstTimes[idx];
                completed[idx] = true;
                completedProcesses++;
            } else {
                currentTime++;
            }
        }

        for (int i = 0; i < n; i++) {
            totalWaitingTime += waitingTime[i];
            totalTurnaroundTime += turnaroundTime[i];
        }

        System.out.println("\nSJF Non-preemptive Scheduling Results:");
        System.out.printf("%-10s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%d\t\t%d\t\t%d\n", i + 1, waitingTime[i], turnaroundTime[i]);
        }
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / n);
    }

    // SJF Preemptive Scheduling
    public static void sjfPreemptive(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        int[] arrivalTimes = new int[n];
        int[] burstTimes = new int[n];
        int[] remainingTime = new int[n];

        System.out.println("Enter Arrival Times:");
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = scanner.nextInt();
        }

        System.out.println("Enter Burst Times:");
        for (int i = 0; i < n; i++) {
            burstTimes[i] = scanner.nextInt();
            remainingTime[i] = burstTimes[i];
        }

        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        Queue<Integer> queue = new LinkedList<>();
        int currentTime = 0;
        int completedProcesses = 0;

        while (completedProcesses < n) {
            for (int i = 0; i < n; i++) {
                if (arrivalTimes[i] <= currentTime && remainingTime[i] > 0) {
                    queue.add(i);
                }
            }

            if (!queue.isEmpty()) {
                int idx = queue.poll();
                remainingTime[idx]--;
                if (remainingTime[idx] == 0) {
                    completedProcesses++;
                    turnaroundTime[idx] = currentTime + 1 - arrivalTimes[idx];
                    waitingTime[idx] = turnaroundTime[idx] - burstTimes[idx];
                }
            }
            currentTime++;
        }

        for (int i = 0; i < n; i++) {
            totalWaitingTime += waitingTime[i];
            totalTurnaroundTime += turnaroundTime[i];
        }

        System.out.println("\nSJF Preemptive Scheduling Results:");
        System.out.printf("%-10s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%d\t\t%d\t\t%d\n", i + 1, waitingTime[i], turnaroundTime[i]);
        }
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / n);
    }

    // Round Robin Scheduling
    public static void roundRobin(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        int[] arrivalTimes = new int[n];
        int[] burstTimes = new int[n];
        int[] remainingTime = new int[n];

        System.out.println("Enter Arrival Times:");
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = scanner.nextInt();
        }

        System.out.println("Enter Burst Times:");
        for (int i = 0; i < n; i++) {
            burstTimes[i] = scanner.nextInt();
            remainingTime[i] = burstTimes[i];
        }

        System.out.print("Enter Time Quantum: ");
        int timeQuantum = scanner.nextInt();

        int[] waitingTime = new int[n];
        int[] turnaroundTime = new int[n];
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        Queue<Integer> queue = new LinkedList<>();

        int currentTime = 0;
        int completedProcesses = 0;

        while (completedProcesses < n) {
            for (int i = 0; i < n; i++) {
                if (arrivalTimes[i] <= currentTime && remainingTime[i] > 0) {
                    queue.add(i);
                }
            }

            if (!queue.isEmpty()) {
                int idx = queue.poll();
                if (remainingTime[idx] > timeQuantum) {
                    currentTime += timeQuantum;
                    remainingTime[idx] -= timeQuantum;
                } else {
                    currentTime += remainingTime[idx];
                    waitingTime[idx] = currentTime - arrivalTimes[idx] - burstTimes[idx];
                    turnaroundTime[idx] = waitingTime[idx] + burstTimes[idx];
                    remainingTime[idx] = 0;
                    completedProcesses++;
                }
            } else {
                currentTime++;
            }
        }

        for (int i = 0; i < n; i++) {
            totalWaitingTime += waitingTime[i];
            totalTurnaroundTime += turnaroundTime[i];
        }

        System.out.println("\nRound Robin Scheduling Results:");
        System.out.printf("%-10s%-15s%-15s\n", "Process", "Waiting Time", "Turnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("P%d\t\t%d\t\t%d\n", i + 1, waitingTime[i], turnaroundTime[i]);
        }
        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / n);
    }

    // Banker's Algorithm
    public static void bankersAlgorithm(Scanner scanner) {
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();
        System.out.print("Enter number of resources: ");
        int m = scanner.nextInt();

        int[][] allocation = new int[n][m];
        int[][] max = new int[n][m];
        int[] available = new int[m];
        int[][] need = new int[n][m];

        System.out.println("Enter Allocation Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter Max Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max[i][j] = scanner.nextInt();
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        System.out.println("Enter Available Resources:");
        for (int j = 0; j < m; j++) {
            available[j] = scanner.nextInt();
        }

        System.out.println("\nNeed Matrix:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(need[i][j] + " ");
            }
            System.out.println();
        }

        boolean[] finished = new boolean[n];
        int[] safeSequence = new int[n];
        int safeSequenceIndex = 0;

        while (safeSequenceIndex < n) {
            boolean found = false;
            for (int i = 0; i < n; i++) {
                if (!finished[i]) {
                    boolean canAllocate = true;
                    for (int j = 0; j < m; j++) {
                        if (need[i][j] > available[j]) {
                            canAllocate = false;
                            break;
                        }
                    }
                    if (canAllocate) {
                        for (int j = 0; j < m; j++) {
                            available[j] += allocation[i][j];
                        }
                        finished[i] = true;
                        safeSequence[safeSequenceIndex++] = i;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                break;
            }
        }

        if (safeSequenceIndex == n) {
            System.out.println("\nThe system is in a safe state.");
            System.out.print("Safe sequence: ");
            for (int i = 0; i < n; i++) {
                System.out.print("P" + safeSequence[i] + " ");
            }
            System.out.println();

            System.out.print("Do you want to request resources? (y/n): ");
            String response = scanner.next();
            if (response.equalsIgnoreCase("y")) {
                int[] request = new int[m];
                System.out.println("Enter resource request:");
                for (int j = 0; j < m; j++) {
                    request[j] = scanner.nextInt();
                }

                boolean canGrantRequest = true;
                for (int j = 0; j < m; j++) {
                    if (request[j] > need[0][j]) {
                        System.out.println("Request cannot be granted.");
                        canGrantRequest = false;
                        break;
                    }
                }

                if (canGrantRequest) {
                    for (int j = 0; j < m; j++) {
                        if (request[j] > available[j]) {
                            System.out.println("Request cannot be granted.");
                            canGrantRequest = false;
                            break;
                        }
                    }
                }

                if (canGrantRequest) {
                    System.out.println("Request can be granted.");
                    for (int j = 0; j < m; j++) {
                        available[j] -= request[j];
                        allocation[0][j] += request[j];
                        need[0][j] -= request[j];
                    }
                    System.out.println("Updated Allocation Matrix:");
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < m; j++) {
                            System.out.print(allocation[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Updated Need Matrix:");
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < m; j++) {
                            System.out.print(need[i][j] + " ");
                        }
                        System.out.println();
                    }
                }
            }
        } else {
            System.out.println("\nThe system is not in a safe state.");
        }
    }

    // FIFO Page Replacement
public static void fifoPageReplacement(Scanner scanner) {
    System.out.print("Enter frame size: ");
    int frameSize = scanner.nextInt();
    System.out.print("Enter number of reference pages: ");
    int numPages = scanner.nextInt();
    int[] referenceString = new int[numPages];

    System.out.println("Enter Reference String:");
    for (int i = 0; i < numPages; i++) {
        referenceString[i] = scanner.nextInt();
    }

    int[] frames = new int[frameSize];
    boolean[] isPageInFrame = new boolean[frameSize];
    int pageHits = 0;
    int totalPageFaults = 0; // Changed variable name to avoid conflict
    int front = 0;

    for (int i = 0; i < numPages; i++) {
        boolean found = false;
        for (int j = 0; j < frameSize; j++) {
            if (frames[j] == referenceString[i]) {
                found = true;
                pageHits++;
                break;
            }
        }
        if (!found) {
            // Page fault occurred
            totalPageFaults++;
            // Check for an empty frame
            int emptyIndex = -1;
            for (int j = 0; j < frameSize; j++) {
                if (!isPageInFrame[j]) {
                    emptyIndex = j;
                    break;
                }
            }

            if (emptyIndex != -1) {
                // Place page in the empty frame
                frames[emptyIndex] = referenceString[i];
                isPageInFrame[emptyIndex] = true;
            } else {
                // Replace the page using FIFO
                frames[front] = referenceString[i];
                front = (front + 1) % frameSize;
            }
        }
    }

    System.out.println("\nFIFO Page Replacement Results:");
    System.out.println("Page Hits: " + pageHits);
    System.out.println("Page Faults: " + totalPageFaults);
    System.out.printf("Hit Ratio: %.2f\n", (double) pageHits / numPages);
    System.out.printf("Miss Ratio: %.2f\n", (double) totalPageFaults / numPages);
}

}

