/**
 * Class that represents a task
 */
public class Task {
    private Integer id = 0;
    private float custo;
    private boolean write;
    private byte value;

    public Task() {
    }

    public Task(Integer id, float custo, boolean write, byte value) {
        this.id = id;
        this.custo = custo;
        this.write = write;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getCusto() {
        return custo;
    }

    public void setCusto(float custo) {
        this.custo = custo;
    }

    public boolean getWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
