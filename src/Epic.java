import java.util.*;


public class Epic extends Task {

    private final Set<Subtask> subtasks = new LinkedHashSet<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public Subtask[] getSubtasks() {
        return subtasks.toArray(Subtask[]::new);
    }

    @Override
    public String toString() {
        return String.format(
                "%s\nSubtasks: %s",
                super.toString(),
                Arrays.toString(subtasks.stream().map(Task::getId).toArray())
        );
    }
}
