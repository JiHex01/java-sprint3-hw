import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Epic extends Task {

    public final List<Subtask> subtasks = new LinkedList<>();

    public Epic(String name, String description) {
        super(name, description);
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

