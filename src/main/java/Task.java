/**
 * Class that represents a task
 */
public class Task {
    private Integer id = 0;
    private double custo;
    private TaskType taskType;
    private Integer value;

    public Task() {}

    public Task(Integer id, double custo, TaskType taskType, Integer value) {
        this.id = id;
        this.custo = custo;
        this.taskType = taskType;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
