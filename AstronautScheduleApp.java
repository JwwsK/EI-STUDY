import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Task class
class Task {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;
    private boolean completed;

    public Task(String description, LocalTime startTime, LocalTime endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s] %s", startTime, endTime, description, priority, completed ? "[Completed]" : "");
    }
}

// TaskFactory for creating tasks
class TaskFactory {
    public Task createTask(String description, String startTime, String endTime, String priority) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        return new Task(description, start, end, priority);
    }
}

// Singleton ScheduleManager class
class ScheduleManager {
    private static ScheduleManager instance = null;
    private List<Task> tasks;

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addTask(Task task) throws Exception {
        if (isConflicting(task)) {
            notifyUserOfConflict(task);
            throw new Exception("Task conflicts with an existing task.");
        }
        tasks.add(task);
        System.out.println("Task added successfully. No conflicts.");
    }

    public void removeTask(String description) {
        boolean removed = tasks.removeIf(task -> task.getDescription().equals(description));
        if (removed) {
            System.out.println("Task removed successfully.");
        } else {
            System.out.println("Error: Task not found.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }

        Collections.sort(tasks, Comparator.comparing(Task::getStartTime));

        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void viewTasksByPriority(String priority) {
        tasks.stream()
            .filter(task -> task.getPriority().equalsIgnoreCase(priority))
            .forEach(System.out::println);
    }

    public void editTask(String description, Task newTask) throws Exception {
        removeTask(description);
        addTask(newTask);
        System.out.println("Task edited successfully.");
    }

    public void markTaskAsCompleted(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equals(description)) {
                task.markAsCompleted();
                System.out.println("Task marked as completed.");
                return;
            }
        }
        System.out.println("Error: Task not found.");
    }

    private boolean isConflicting(Task newTask) {
        for (Task task : tasks) {
            if (newTask.getStartTime().isBefore(task.getEndTime()) && newTask.getEndTime().isAfter(task.getStartTime())) {
                return true;
            }
        }
        return false;
    }

    private void notifyUserOfConflict(Task task) {
        System.out.println("Error: Task conflicts with an existing task.");
    }
}

// Main application class
public class AstronautScheduleApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ScheduleManager manager = ScheduleManager.getInstance();
        TaskFactory taskFactory = new TaskFactory();

        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Edit Task");
            System.out.println("5. Mark Task as Completed");
            System.out.println("6. View Tasks by Priority");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            try {
                switch (option) {
                    case 1:
                        System.out.print("Enter task description: ");
                        String description = scanner.nextLine();
                        System.out.print("Enter start time (HH:mm): ");
                        String startTime = scanner.nextLine();
                        System.out.print("Enter end time (HH:mm): ");
                        String endTime = scanner.nextLine();
                        System.out.print("Enter priority level (Low/Medium/High): ");
                        String priority = scanner.nextLine();

                        Task task = taskFactory.createTask(description, startTime, endTime, priority);
                        manager.addTask(task);
                        break;

                    case 2:
                        System.out.print("Enter task description to remove: ");
                        String taskToRemove = scanner.nextLine();
                        manager.removeTask(taskToRemove);
                        break;

                    case 3:
                        manager.viewTasks();
                        break;

                    case 4:
                        System.out.print("Enter task description to edit: ");
                        String taskToEdit = scanner.nextLine();
                        System.out.print("Enter new task description: ");
                        String newDescription = scanner.nextLine();
                        System.out.print("Enter new start time (HH:mm): ");
                        String newStartTime = scanner.nextLine();
                        System.out.print("Enter new end time (HH:mm): ");
                        String newEndTime = scanner.nextLine();
                        System.out.print("Enter new priority level: ");
                        String newPriority = scanner.nextLine();

                        Task editedTask = taskFactory.createTask(newDescription, newStartTime, newEndTime, newPriority);
                        manager.editTask(taskToEdit, editedTask);
                        break;

                    case 5:
                        System.out.print("Enter task description to mark as completed: ");
                        String taskToComplete = scanner.nextLine();
                        manager.markTaskAsCompleted(taskToComplete);
                        break;

                    case 6:
                        System.out.print("Enter priority level (Low/Medium/High): ");
                        String priorityLevel = scanner.nextLine();
                        manager.viewTasksByPriority(priorityLevel);
                        break;

                    case 7:
                        System.out.println("Exiting application.");
                        return;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
