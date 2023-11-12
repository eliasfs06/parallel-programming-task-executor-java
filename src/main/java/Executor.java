import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents an executor. An executor must send tasks to the workers.
 */
public class Executor {
    Deque<Task> taskQueue = new ArrayDeque<>();
    List<Result> results = new ArrayList<>();
    private AtomicInteger resultId = new AtomicInteger(0);

    public Executor() {}

    public Executor(Deque<Task> taskQueue, List<Result> results) {
        this.taskQueue = taskQueue;
        this.results = results;
    }

    public void addTaskToQueue(Task task){
        taskQueue.addLast(task);
    }

    public List<Task> getNTasks(int nTasks){
        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i < nTasks; i++){
            Task task = taskQueue.getLast();
            taskQueue.remove(task);
            tasks.add(task);
        }
        return tasks;
    }

    public Task getTask(){
        Task task = taskQueue.getLast();
        taskQueue.remove(task);
        return task;
    }

    public void shuffleTaskQueue() {
        List<Task> listaDeTasks = (List<Task>) Arrays.asList(taskQueue.toArray(new Task[taskQueue.size()]));
        Collections.shuffle(listaDeTasks);
        taskQueue.clear();
        taskQueue.addAll(listaDeTasks);
    }

    public void addResult(long executionTime, String value){
        results.add(new Result(resultId.getAndIncrement(), value, executionTime));
    }

    public Deque<Task> getTaskQueue() {
        return taskQueue;
    }

    public void setTaskQueue(Deque<Task> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
