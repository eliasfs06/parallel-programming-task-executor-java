import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Executor {
    private Deque<Task> taskQueue = new ArrayDeque<>();
    private ConcurrentLinkedQueue<Result> results = new ConcurrentLinkedQueue<>();
    private AtomicInteger resultId = new AtomicInteger(0);

    public void addTaskToQueue(Task task) {
        taskQueue.addLast(task);
    }

    public List<Task> getNTasks(int nTasks) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < nTasks; i++) {
            tasks.add(taskQueue.removeFirst());
        }
        return tasks;
    }

    public void shuffleTaskQueue() {
        List<Task> taskList = new ArrayList<>(taskQueue);
        Collections.shuffle(taskList);
        taskQueue = new ArrayDeque<>(taskList);
    }

    public Task getTask() {
        return taskQueue.removeFirst();
    }


    public void addResult(long executionTime, String value) {
        results.add(new Result(resultId.getAndIncrement(), value, executionTime));
    }

    public Deque<Task> getTaskQueue() {
        return taskQueue;
    }

    public ConcurrentLinkedQueue<Result> getResults() {
        return results;
    }
}