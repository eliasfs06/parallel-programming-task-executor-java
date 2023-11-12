import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that represents a worker. Workers must execute a task.
 */
public class Worker extends Thread {
    private List<Task> tasks = new ArrayList<>();
    private File sharedFile;
    private Executor executor;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Worker() {}

    public Worker(List<Task> tasks, File sharedFile, Executor executor) {
        this.tasks = tasks;
        this.sharedFile = sharedFile;
        this.executor = executor;
    }

    @Override
    public void run(){
        for(Task task : tasks){
            long startTime = System.nanoTime();
            String value = null;
            try {
                if (task.getTaskType() == TaskType.WRITING) value = write(task);
                else value = read(task);
            } catch (IOException | InterruptedException e){
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            long executionTime = endTime - startTime;
            this.setResult(executionTime, value);
        }
    }

    synchronized public void setResult(long executionTime, String value){
        executor.addResult(executionTime, value);
    }

    public String write(Task task) throws IOException, InterruptedException {
        Thread.sleep(task.getCusto());
        lock.writeLock().lock();
        Integer newValue = null;
        try (RandomAccessFile raf = new RandomAccessFile(sharedFile, "rw")) {
            newValue = Integer.valueOf(raf.readLine()) + task.getValue();
            raf.seek(0);
            raf.writeBytes((newValue) + System.lineSeparator());
            System.out.println("W - Task " + this.getId() + " : " + newValue);
        } finally {
            lock.writeLock().unlock();
        }
        return String.valueOf(newValue);
    }

    public String read(Task task) throws IOException, InterruptedException {
        Thread.sleep(task.getCusto());
        lock.readLock().lock();
        String value;
        try (RandomAccessFile raf = new RandomAccessFile(sharedFile, "r")) {
            value = raf.readLine();
            System.out.println("R - Task " + this.getId() + " : " + value);
        } finally {
            lock.readLock().unlock();
        }
        return value;
    }


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public File getSharedFile() {
        return sharedFile;
    }

    public void setSharedFile(File sharedFile) {
        this.sharedFile = sharedFile;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

}