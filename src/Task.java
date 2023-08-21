public class Task {

    private long id;
    private final String name;
    private final String description;
    private Status status = Status.NEW;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s [%d] %s\n%s", this.name, this.id, this.status, this.description);
    }
}
