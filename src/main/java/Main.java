import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    protected static String FILE_PATH = "./shared-file.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nTasksAmount = getInput(scanner, "Digite o valor de N: ");
        int tWorkersThreads = getInput(scanner, "Digite o valor de T: ");
        int eWrittingTasks = getInput(scanner, "Digite o valor de E: ");

        double tasksAmount = Math.pow(10, nTasksAmount);
        Executor executor = new Executor((int) tasksAmount);
        createTasks(executor, tasksAmount, eWrittingTasks);

        File sharedFile = createSharedFile();
        List<Worker> workers = initializeWorkers(executor, tasksAmount, tWorkersThreads, sharedFile);

        long executionTime = executeWorkers(workers);
        System.out.print("Fim do processamento.");

        createResultFile(nTasksAmount, tWorkersThreads, eWrittingTasks, executionTime, executor);
    }

    private static int getInput(Scanner scanner, String message) {
        System.out.print(message);
        return scanner.nextInt();
    }

    private static void createTasks(Executor executor, double tasksAmount, int eWrittingTasks) {
        double writingTasks = ((double) eWrittingTasks / 100) * tasksAmount;
        double readingTasks = tasksAmount - writingTasks;
        int taskId = 0;

        for (int i = 0; i < (int) writingTasks; i++) {
            executor.addTaskToQueue(new Task(++taskId, (Math.random() * 0.01), TaskType.WRITING, (int) (Math.random() * 10)));
        }
        for (int i = 0; i < (int) readingTasks; i++) {
            executor.addTaskToQueue(new Task(++taskId, (Math.random() * 0.01), TaskType.READING, (int) (Math.random() * 10)));
        }
        executor.shuffleTaskQueue();
    }

    private static File createSharedFile() {
        File sharedFile = new File(FILE_PATH);
        try {
            sharedFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(new FileWriter(sharedFile));
            printWriter.println("0");
            printWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sharedFile;
    }

    private static List<Worker> initializeWorkers(Executor executor, double tasksAmount, int tWorkersThreads, File sharedFile) {
        int nTasksPerWorkers = (int) tasksAmount / tWorkersThreads;
        int restTasks = (int) (tasksAmount - (nTasksPerWorkers * tWorkersThreads));

        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < tWorkersThreads; i++) {
            Worker worker = new Worker(executor.getNTasks(nTasksPerWorkers), sharedFile, executor);
            workers.add(worker);
        }
        distributeRemainingTasks(executor, restTasks, workers);
        return workers;
    }

    private static void distributeRemainingTasks(Executor executor, int restTasks, List<Worker> workers) {
        while (restTasks > 0) {
            for (Worker worker : workers) {
                if(restTasks == 0) break;
                if (!(executor.getTaskQueue().length == 0)) {
                    worker.getTasks().add(executor.getTask());
                    restTasks--;
                }
            }
        }
    }

    private static long executeWorkers(List<Worker> workers) {
        long startTime = System.nanoTime();
        workers.forEach(Thread::start);
        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;
        return executionTime;
    }

    private static void createResultFile(int nTasksAmount, int tWorkersThreads, int eWrittingTasks, long executionTime, Executor executor) {
        File result = new File("src/main/result/N" + nTasksAmount + "T" + tWorkersThreads + "E" + eWrittingTasks);
        try {
            result.createNewFile();
            PrintWriter printWriterResult = new PrintWriter(new FileWriter(result));
            printWriterResult.println("Resultados");
            printWriterResult.println("Tempo total de processamento: " + executionTime + " nanosegundos");
            printWriterResult.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}