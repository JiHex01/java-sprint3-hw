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

    private void createTask(Task Objective) {
        var task = new Task(Objective.getName(), Objective.getDescription());
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

        if (task instanceof Subtask && ((Subtask) task).getEpic() != null) {
            Subtask subtask = (Subtask) task;
            var epic = subtask.getEpic();
            boolean allDone = true;

            for (Subtask subtask1 : epic.subtasks) {
                if (subtask1.getStatus().equals(Status.NEW)) {
                    epic.setStatus(Status.IN_PROGRESS);
                    break;
                }

                if (!subtask1.getStatus().equals(Status.DONE)) {
                    allDone = false;
                    break;
                }
            }

            if (allDone) {
                epic.setStatus(Status.DONE);
            }
        }
    }


    private void setStatus(Task task, Status status) {
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
        System.out.println("-----Task list end -----");
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
        manager.getAll().forEach(task -> manager.setStatus(task, Status.IN_PROGRESS));
        manager.printAll();
        manager.getAll().forEach(task -> manager.setStatus(task, Status.DONE));
        manager.printAll();
    }
}
