/**
 * Class that represents the result of a task after being processed
 */
public class Result {
    private Integer id;
    private String result;
    private Long time;

    public Result() {}

    public Result(Integer id, String result, Long time) {
        this.id = id;
        this.result = result;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
