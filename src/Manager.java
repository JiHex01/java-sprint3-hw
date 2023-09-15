import java.util.*;

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

    private void updateEpicName(Epic epic, String name) {
        epic.setName(name);
        update(epic);
    }

    private void updateEpicDescription(Epic epic, String description) {
        epic.setDescription(description);
        update(epic);
    }

    private void deleteTaskById(long id) {
        Task task = tasks.get(id);
        if (task instanceof Epic) {
            deleteEpicById(id);
        } else if (task instanceof Subtask) {
            deleteSubtaskById(id);
        } else {
            tasks.remove(id);
        }
    }

    private void deleteEpicById(long id) {
        Epic epic = (Epic) tasks.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                deleteSubtaskById(subtask.getId());
            }
        }
    }

    private void deleteSubtaskById(long id) {
        Subtask subtask = (Subtask) tasks.remove(id);
        if (subtask != null) {
            Epic epic = (Epic) tasks.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
            }
        }
    }

    private Subtask[] getSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    void setStatus(Task task, Status status) {
        if (task instanceof Epic) return;

        task.setStatus(status);
        update(task);
    }
    public List<Task> getAllTasks() {
        return new LinkedList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        List<Epic> epics = new LinkedList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                epics.add((Epic) task);
            }
        }
        return epics;
    }

    public List<Subtask> getAllSubtasks() {
        List<Subtask> subtasks = new LinkedList<>();
        for (Task task : tasks.values()) {
            if (task instanceof Subtask) {
                subtasks.add((Subtask) task);
            }
        }
        return subtasks;
    }

    public List<Subtask> getAllEpicSubtasks(Epic epic) {
        List<Subtask> epicSubtasks = new LinkedList<>();
        Collections.addAll(epicSubtasks, epic.getSubtasks());
        return epicSubtasks;
    }

    public List<String> printAll() {
        List<String> taskStrings = new ArrayList<>();
        taskStrings.add("----- Tasks list start -----");

        taskStrings.add("All Tasks:");
        List<Task> tasks = getAllTasks();
        for (Task task : tasks) {
            taskStrings.add(task.toString());
        }

        taskStrings.add("All Epics:");
        List<Epic> epics = getAllEpics();
        for (Epic epic : epics) {
            taskStrings.add(epic.toString());
            taskStrings.add("All Subtasks for Epic " + epic.getId() + ":");
            List<Subtask> epicSubtasks = getAllEpicSubtasks(epic);
            for (Subtask subtask : epicSubtasks) {
                taskStrings.add(subtask.toString());
            }
        }

        taskStrings.add("All Subtasks:");
        List<Subtask> subtasks = getAllSubtasks();
        for (Subtask subtask : subtasks) {
            taskStrings.add(subtask.toString());
        }

        taskStrings.add("----- Task list end -----");

        return taskStrings;
    }


}