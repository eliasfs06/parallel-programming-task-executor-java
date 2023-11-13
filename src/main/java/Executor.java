import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents an executor. An executor must send tasks to the workers.
 */
public class Executor {
    Deque<Task> taskQueue = new ArrayDeque<>();
    CopyOnWriteArrayList<Result> results = new CopyOnWriteArrayList<>();
    private AtomicInteger resultId = new AtomicInteger(0);

    public Executor() {}

    public Executor(Deque<Task> taskQueue, CopyOnWriteArrayList<Result> results) {
        this.taskQueue = taskQueue;
        this.results = results;
    }

    public void addTaskToQueue(Task task){
        taskQueue.addLast(task);
    }

    public List<Task> getNTasks(int nTasks){
        List<Task> tasks = new ArrayList<>();
        for(int i = 0; i < nTasks; i++){
            Task task = taskQueue.removeFirst();
            tasks.add(task);
        }
        return tasks;
    }

    public Task getTask(){
        return taskQueue.removeFirst();
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

    public CopyOnWriteArrayList<Result> getResults() {
        return results;
    }

    public void setResults(CopyOnWriteArrayList<Result> results) {
        this.results = results;
    }

}
