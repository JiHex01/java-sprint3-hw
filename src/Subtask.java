public class Subtask extends Task {

    private Epic epic;

    public Subtask(String name, String description) {
        super(name, description);

    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return String.format(
                "%s\nEpic: %s",
                super.toString(),
                epic != null ? epic.getId() : "null"
        );
    }
}

