import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Manager {

    private long taskId = 1;
    private final Map<Long, Task> tasks = new HashMap<>();

    List<Task> getAll() {
        return new LinkedList<>(tasks.values());
    }

    private void deleteAll() {
        tasks.clear();
    }

    Task getById(long id) {
        return this.tasks.get(id);
    }

    long createTask(Task blueprint) {
        var task = new Task(blueprint.getName(), blueprint.getDescription());
        task.setId(taskId++);
        update(task);
        return task.getId();
    }

    long createEpic(Task blueprint) {
        var epic = new Epic(blueprint.getName(), blueprint.getDescription());
        epic.setId(taskId++);

        update(epic);
        return epic.getId();
    }

    long createSubtask(Task blueprint) {
        var subtask = new Subtask(blueprint.getName(), blueprint.getDescription());
        subtask.setId(taskId++);
        update(subtask);
        return subtask.getId();
    }

    void update(Task task) {
        tasks.put(task.getId(), task);
    }

    private void update(Subtask subtask) {
        update((Task) subtask);

        if (subtask.getEpic() == null) return;

        var epic = subtask.getEpic();
        for (Subtask subtask1 : getSubtasks(epic)) {
            if (subtask1.getStatus().equals(Status.NEW))
                continue;

            epic.setStatus(Status.IN_PROGRESS);
            break;
        }

        for (Subtask subtask1 : getSubtasks(epic)) {
            if (!subtask1.getStatus().equals(Status.DONE))
                break;

            epic.setStatus(Status.DONE);
        }
    }

    private void update(Epic epic) {
        update((Task) epic);
    }

    private void deleteById(long id) {
        this.tasks.remove(id);
    }

    private Subtask[] getSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    void setStatus(Task task, Status status) {
        if (task instanceof Epic) return;

        task.setStatus(status);
        update(task);
    }

    void printAll() {
        System.out.println();
        System.out.println("----- Tasks list start -----");
        for (Task task : getAll()) {
            System.out.println(task);
            System.out.println();
        }
        System.out.println("----- Task list end -----");
        System.out.println();
    }


}