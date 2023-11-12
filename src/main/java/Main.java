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

        System.out.print("Digite o valor de N: ");
        int nTasksAmount = scanner.nextInt();

        System.out.print("Digite o valor de T: ");
        int tWorkersThreads = scanner.nextInt();

        System.out.print("Digite o valor de E: ");
        int eWrittingTasks = scanner.nextInt();

        Executor executor = new Executor();

        double tasksAmount = Math.pow(10, nTasksAmount);
        double writingTasks = ((double) eWrittingTasks / 100) * tasksAmount;
        double readingTasks = tasksAmount - writingTasks;
        int taskId = 0;

        //Creates tasks queue
        for(int i = 0; i < writingTasks; i++) {
            executor.addTaskToQueue(new Task(++taskId, (int) (Math.random() * 0.01), TaskType.WRITING, (int) (Math.random() * 10)));
        }
        for(int i = 0; i < readingTasks; i++) {
            executor.addTaskToQueue(new Task(++taskId, (int) (Math.random() * 0.01), TaskType.READING, (int) (Math.random() * 10)));
        }
        executor.shuffleTaskQueue();

        //Creates shared fil
        File sharedFile = new File(FILE_PATH);
        try {
            sharedFile.createNewFile();
            PrintWriter printWriter = new PrintWriter(new FileWriter(sharedFile));
            printWriter.println("0");
            printWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Number of tasks per worker0
        int nTasksPerWorkers = (int) tasksAmount / tWorkersThreads;
        int restTasks = (int) (tasksAmount - (nTasksPerWorkers * tWorkersThreads));

        //Initializes workers
        List<Worker> workers = new ArrayList<>();
        for(int i = 0; i < tWorkersThreads; i++){
            Worker worker = new Worker(executor.getNTasks(nTasksPerWorkers), sharedFile, executor);
            workers.add(worker);
        }
        //Distributes tasks left over in the division
        while (restTasks > 0) {
            for (Worker worker : workers) {
                if (!executor.getTaskQueue().isEmpty()) {
                    worker.getTasks().add(executor.getTask());
                    restTasks--;
                }
            }
        }

        //Workers working
        long startTime = System.nanoTime();
        workers.forEach(Thread::start);
        long endTime = System.nanoTime();
        long executionTime = endTime - startTime;

        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print("Fim do processamento.");

        //Creates result file
        File result = new File("src/main/result/N"+nTasksAmount+"T"+tWorkersThreads+"E"+eWrittingTasks);
        try {
            result.createNewFile();
            PrintWriter printWriterResult = new PrintWriter(new FileWriter(result));
            printWriterResult.println("Resultados");
            printWriterResult.println("Tempo total de processamento: " + executionTime + " nanosegundos");
            for(Result res : executor.getResults()){
                printWriterResult.println("Resultado " + res.getId() + ": value = " + res.getResult() + ", tempo de execução = " + res.getTime() + " nanosegundos");
            }
            printWriterResult.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
