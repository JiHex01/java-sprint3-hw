public class Main {
    public static void main(String[] args) {
        var manager = new Manager();

        manager.createTask(new Task("Task 1", "Description 1"));
        manager.createTask(new Task("Task 2", "Description 2"));

        var epic1 = (Epic) manager.getById(manager.createEpic(new Epic("Epic 1", "Awesome description 1")));
        var subtask1 = (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 1", "Nice description 1")));
        var subtask2 = (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 2", "Nice description 2")));
        epic1.addSubtask(subtask1);
        epic1.addSubtask(subtask2);
        manager.update(epic1);

        var epic3 = (Epic) manager.getById(manager.createEpic(new Epic("Epic 2", "Awesome description 2")));
        var subtask3 = (Subtask) manager.getById(manager.createSubtask(new Subtask("Subtask 3", "Nice description 3")));
        epic3.addSubtask(subtask3);



        manager.printAll();
        manager.getAll().forEach(task -> manager.setStatus(task, Status.IN_PROGRESS));
        manager.printAll();
        manager.getAll().forEach(task -> manager.setStatus(task, Status.DONE));
        manager.printAll();
    }
}
