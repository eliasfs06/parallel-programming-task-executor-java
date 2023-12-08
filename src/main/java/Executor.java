import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Executor {
    private Task[] taskQueue;
    private Result[] results;
    private AtomicInteger resultId = new AtomicInteger(0);
    private int currentResultIndex = 0;
    private int currentTaskAddIndex = 0;
    private int currentTaskRemoveIndex = 0;

    public Executor(int tasksAmount) {
        this.taskQueue = new Task[tasksAmount];
        this.results = new Result[tasksAmount];
    }

    public void addTaskToQueue(Task task) {
        taskQueue[currentTaskAddIndex++] = task;
    }

    public List<Task> getNTasks(int nTasks) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < nTasks; i++) {
            tasks.add(taskQueue[currentTaskRemoveIndex++]);
        }
        return tasks;
    }

    public void shuffleTaskQueue() {
        List<Task> taskList = new ArrayList<>(Arrays.asList(taskQueue));
        Collections.shuffle(taskList);
        taskQueue = taskList.toArray(new Task[0]);
    }

    public Task getTask() {
        return taskQueue[currentTaskRemoveIndex++];
    }

    public void addResult(long executionTime, String value) {
        results[currentResultIndex++] = new Result(resultId.getAndIncrement(), value, executionTime);
    }

    public Task[] getTaskQueue() {
        return taskQueue;
    }

    public Result[] getResults() {
        return results;
    }
}