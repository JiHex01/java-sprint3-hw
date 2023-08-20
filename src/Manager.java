import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Manager {

    private long taskId = 1;
    private final Map<Long, Task> tasks = new HashMap<>();

    private List<Task> getAll() {
        return new LinkedList<>(tasks.values());
    }

    private void deleteAll() {
        tasks.clear();
    }

    private Task getById(long id) {
        return this.tasks.get(id);
    }

    private void createTask(Task blueprint) {
        var task = new Task(blueprint.getName(), blueprint.getDescription());
        task.setId(taskId++);
        update(task);
    }

    private void createEpic(Task blueprint, Subtask... subtasks) {
        var epic = new Epic(blueprint.getName(), blueprint.getDescription());
        epic.setId(taskId++);
        for (Subtask subtask : subtasks) {
            epic.subtasks.add(subtask);
            subtask.setEpic(epic);
        }

        update(epic);
    }

    private long createSubtask(Task blueprint) {
        var subtask = new Subtask(blueprint.getName(), blueprint.getDescription());
        subtask.setId(taskId++);
        update(subtask);
        return subtask.getId();
    }

    private void update(Task task) {
        tasks.put(task.getId(), task);

        if (task.getClass() == Subtask.class && ((Subtask) task).getEpic() != null) {
            Subtask subtask = (Subtask) task;
            var epic = subtask.getEpic();

            for (Subtask subtask1 : epic.subtasks) {
                if (subtask1.getStatus().equals(Task.Status.NEW))
                    continue;

                epic.setStatus(Task.Status.IN_PROGRESS);
                break;
            }

            for (Subtask subtask1 : epic.subtasks) {
                if (!subtask1.getStatus().equals(Task.Status.DONE))
                    break;

                epic.setStatus(Task.Status.DONE);
            }
        }
    }


    private void setStatus(Task task, Task.Status status) {
        if (task instanceof Epic) return;

        task.setStatus(status);
        update(task);
    }

    private void printAll() {
        System.out.println();
        System.out.println("----- Tasks list start -----");
        for (Task task : getAll()) {
            System.out.println(task);
            System.out.println();
        }
        System.out.println("----- Task list end -----");
        System.out.println();
    }

    public static void main(String[] args) {
        var manager = new Manager();

        manager.createTask(new Task("Task 1", "Description 1"));
        manager.createTask(new Task("Task 2", "Description 2"));

        manager.createEpic(
                new Epic("Epic 1", "Awesome description 1"),
                (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 1", "Nice description 1"))),
                (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 2", "Nice description 2")))
        );

        manager.createEpic(
                new Epic("Epic 2", "Awesome description 2"),
                (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 3", "Nice description 3"))
                ));


        manager.printAll();
        manager.getAll().forEach(task -> manager.setStatus(task, Task.Status.IN_PROGRESS));
        manager.printAll();
        manager.getAll().forEach(task -> manager.setStatus(task, Task.Status.DONE));
        manager.printAll();
    }
}
