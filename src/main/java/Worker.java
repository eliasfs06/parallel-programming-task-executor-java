import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Worker extends Thread {
    private List<Task> tasks;
    private File sharedFile;
    private Executor executor;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Worker(List<Task> tasks, File sharedFile, Executor executor) {
        this.tasks = tasks;
        this.sharedFile = sharedFile;
        this.executor = executor;
    }

    @Override
    public void run() {
        for (Task task : tasks) {
            executeTask(task);
        }
    }

    private void executeTask(Task task) {
        try {
            long startTime = System.nanoTime();
            Thread.sleep((long) (task.getCusto() * 100));
            String value = task.getWrite() ? write(task) : read(task);
            long executionTime = System.nanoTime() - startTime;
            executor.addResult(executionTime, value);
            System.out.println("Thread " + Thread.currentThread().getId() + " executed task " + task.getId() + " in " + executionTime + " nanoseconds");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String write(Task task) throws IOException {
        lock.writeLock().lock();
        try (RandomAccessFile raf = new RandomAccessFile(sharedFile, "rw")) {
            int newValue = Integer.parseInt(raf.readLine()) + task.getValue();
            raf.seek(0);
            raf.writeBytes(newValue + System.lineSeparator());
            return String.valueOf(newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private String read(Task task) throws IOException {
        lock.readLock().lock();
        try (RandomAccessFile raf = new RandomAccessFile(sharedFile, "r")) {
            return raf.readLine();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }
}