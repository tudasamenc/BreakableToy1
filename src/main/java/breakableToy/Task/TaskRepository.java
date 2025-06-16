package breakableToy.Task;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class for managing Task entities. Provides CRUD operations and additional 
 * functionality for task management including searching, filtering, sorting and pagination.
 *
 * @Repository annotation indicates that this class is a Spring Data Repository
 */
@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();
    private final ChatClient chatClient;

    public TaskRepository(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    // Find every task
    List<Task> findAll() {
        return tasks;
    }

    // Find all tasks that are done
    List<Task> findAllByDone(boolean done) {
        return tasks.stream()
                .filter(task -> task.done() == done)
                .toList();
    }

    int size() {
        return tasks.size();
    }

    Task findElementAt(int i) {
        return tasks.get(i);
    }

    List<Task> findAllByPriority(Integer priority) {
        return tasks.stream()
                .filter(task -> task.priority().equals(priority))
                .toList();
    }

    List<Task> findAllMasked(boolean P, Integer priority, boolean D, boolean done, boolean N, String name) {
        return tasks.stream()
                .filter(task -> P || task.priority().equals(priority))
                .filter(task -> D || task.done() == done)
                .filter(task -> N || task.name().contains(name))
                .toList();
    }

    List<Task> sortByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(Task::priority))
                .toList();
    }

    Optional<Task> findById(Integer id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }

    Optional<Task> setDone(Integer id, boolean done) {
        Optional<Task> taskOpt = findById(id);
        taskOpt.ifPresent(task -> {
            Task updatedTask = new Task(task.id(), task.name(), done, task.priority(), task.dueDate(),
                    done ? LocalDateTime.now() : null, task.creationDate());
            tasks.set(tasks.indexOf(task), updatedTask);
        });
        return taskOpt;
    }

    void create(Task task) {
        tasks.add(task);
    }

    void update(Task task, Integer id) {
        Optional<Task> existingTask = findById(id);
        existingTask.ifPresent(existing -> {
            Task updatedTask = new Task(
                    id,
                    task.name() != null ? task.name() : existing.name(),
                    task.done(),
                    task.priority() != null ? task.priority() : existing.priority(),
                    task.dueDate() != null ? task.dueDate() : existing.dueDate(),
                    task.doneDate() != null ? task.doneDate() : existing.doneDate(),
                    task.creationDate() != null ? task.creationDate() : existing.creationDate()
            );
            tasks.set(tasks.indexOf(existing), updatedTask);
        });
    }

    void delete(Integer id) {
        tasks.removeIf(task -> task.id().equals(id));
    }

    //  Pagination only
    public List<Task> findPaginated(int page, int size) {
        int fromIndex = page * size;
        if (fromIndex >= tasks.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, tasks.size());
        return tasks.subList(fromIndex, toIndex);
    }

    //  Sorting with ascending/descending order
    public List<Task> findSorted(
            boolean sortByPriority,
            boolean priorityAsc,
            boolean sortByDone,
            boolean doneAsc
    ) {
        return tasks.stream()
                .sorted((t1, t2) -> {
                    int result = 0;

                    if (sortByPriority) {
                        result = Integer.compare(t1.priority(), t2.priority());
                        if (!priorityAsc) result *= -1;
                        if (result != 0) return result;
                    }

                    if (sortByDone) {
                        result = Boolean.compare(t1.done(), t2.done());
                        if (!doneAsc) result *= -1;
                        if (result != 0) return result;
                    }

                    return result;
                })
                .toList();
    }

    public String adviceCall(int id){
        String name = tasks.stream()
                .filter(task -> task.id().equals(id))
                .map(Task::name)
                .findFirst()
                .orElse(null);
            System.out.println(name);

        return this.chatClient.prompt()
                .user("Give me a small list of steps I could follow to get this task done. Each step must have just a few words: "+name)
                .call()
                .content();
    }

    public TaskCompletionStats calculateAverageCompletionTimes() {
        // Get all completed tasks with valid done dates
        List<Task> completedTasks = tasks.stream()
                .filter(Task::done)
                .filter(task -> task.doneDate() != null && task.creationDate() != null)
                .toList();

        // Calculate overall average
        double overallAverage = calculateAverageHours(completedTasks);

        // Calculate averages per priority
        double priority1Average = calculateAverageHours(filterByPriority(completedTasks, 1));
        double priority2Average = calculateAverageHours(filterByPriority(completedTasks, 2));
        double priority3Average = calculateAverageHours(filterByPriority(completedTasks, 3));

        return new TaskCompletionStats(
                overallAverage,
                priority1Average,
                priority2Average,
                priority3Average
        );
    }

    private double calculateAverageHours(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return 0.0;
        }

        double totalHours = tasks.stream()
                .mapToDouble(task -> {
                    long hours = java.time.Duration.between(
                            task.creationDate(),
                            task.doneDate()
                    ).toHours();
                    return hours;
                })
                .sum();

        return totalHours / tasks.size();
    }

    private List<Task> filterByPriority(List<Task> tasks, int priority) {
        return tasks.stream()
                .filter(task -> task.priority() == priority)
                .toList();
    }



    //  Combined sorting and pagination
    public List<Task> findSortedAndPaginated(
            int sortType,           // 0: none, 1: priority, 2: done, 3: name, 4: due date
            boolean asc,
            int page,
            int size,
            String search,
            boolean filterByDoneEnabled,
            boolean doneFilterValue,
            boolean filterByPriorityEnabled,
            int priorityFilterValue
    ) {
        List<Task> tasks = findAll(); // You should replace this with your source of unsorted tasks

        // Sort based on sortType
        Comparator<Task> comparator = null;
        switch (sortType) {
            case 1 -> comparator = Comparator.comparing(Task::priority, Comparator.nullsLast(Integer::compareTo));
            case 2 -> comparator = Comparator.comparing(Task::done);
            case 3 -> comparator = Comparator.comparing(Task::name, Comparator.nullsLast(String::compareToIgnoreCase));
            case 4 -> comparator = Comparator.comparing(Task::dueDate, Comparator.nullsLast(LocalDateTime::compareTo));
        }

        if (comparator != null) {
            if (!asc) {
                comparator = comparator.reversed();
            }
            tasks = tasks.stream().sorted(comparator).toList();
        }

        // Filter by search string in "name"
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            tasks = tasks.stream()
                    .filter(task -> task.name() != null && task.name().toLowerCase().contains(searchLower))
                    .toList();
        }

        // Filter by done status if enabled
        if (filterByDoneEnabled) {
            tasks = tasks.stream()
                    .filter(task -> task.done() == doneFilterValue)
                    .toList();
        }

        // Filter by priority if enabled
        if (filterByPriorityEnabled) {
            tasks = tasks.stream()
                    .filter(task -> task.priority() != null && task.priority().equals(priorityFilterValue))
                    .toList();
        }

        // Pagination
        int fromIndex = page * size;
        if (fromIndex >= tasks.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, tasks.size());
        return tasks.subList(fromIndex, toIndex);
    }


    // Initial data
    @PostConstruct
    private void init() {
        tasks.add(new Task(1, "Buy groceries", true, 2,
                LocalDateTime.of(2025, Month.JUNE, 25, 15, 0),
                LocalDateTime.of(2025, Month.JUNE, 24, 10, 0),
                LocalDateTime.of(2025, Month.MAY, 17, 9, 30)));

        tasks.add(new Task(2, "Finish book chapter", false, 3,
                LocalDateTime.of(2025, Month.JULY, 2, 20, 0),
                null,
                LocalDateTime.of(2025, Month.MAY, 18, 14, 15)));

        tasks.add(new Task(3, "Clean garage", true, 1,
                LocalDateTime.of(2025, Month.JUNE, 29, 11, 0),
                LocalDateTime.of(2025, Month.JUNE, 27, 16, 45),
                LocalDateTime.of(2025, Month.MAY, 16, 11, 0)));

        tasks.add(new Task(4, "Call the bank", false, 2,
                LocalDateTime.of(2025, Month.JULY, 10, 13, 0),
                null,
                LocalDateTime.of(2025, Month.MAY, 20, 10, 0)));

        tasks.add(new Task(5, "Water the plants", true, 1,
                LocalDateTime.of(2025, Month.JUNE, 23, 8, 0),
                LocalDateTime.of(2025, Month.JUNE, 22, 7, 0),
                LocalDateTime.of(2025, Month.MAY, 15, 7, 30)));

        tasks.add(new Task(6, "Organize desk", false, 2,
                LocalDateTime.of(2025, Month.JULY, 5, 17, 0),
                null,
                LocalDateTime.of(2025, Month.MAY, 29, 17, 20)));

        tasks.add(new Task(7, "Reply to emails", true, 3,
                LocalDateTime.of(2025, Month.JUNE, 30, 9, 0),
                LocalDateTime.of(2025, Month.JUNE, 29, 8, 30),
                LocalDateTime.of(2025, Month.MAY, 31, 12, 0)));

        tasks.add(new Task(8, "Pay electricity bill", false, 1,
                LocalDateTime.of(2025, Month.JULY, 13, 23, 59),
                null,
                LocalDateTime.of(2025, Month.MAY, 27, 18, 10)));

        tasks.add(new Task(9, "Walk the dog", true, 2,
                LocalDateTime.of(2025, Month.JUNE, 22, 6, 0),
                LocalDateTime.of(2025, Month.JUNE, 21, 6, 15),
                LocalDateTime.of(2025, Month.MAY, 21, 6, 0)));

        tasks.add(new Task(10, "Prepare lunch", false, 3,
                LocalDateTime.of(2025, Month.JUNE, 26, 13, 30),
                null,
                LocalDateTime.of(2025, Month.MAY, 30, 11, 45)));

        tasks.add(new Task(11, "Schedule dentist appointment", true, 2,
                LocalDateTime.of(2025, Month.JULY, 3, 10, 0),
                LocalDateTime.of(2025, Month.JUNE, 28, 10, 30),
                LocalDateTime.of(2025, Month.MAY, 19, 9, 0)));

        tasks.add(new Task(12, "Do laundry", false, 1,
                LocalDateTime.of(2025, Month.JUNE, 21, 14, 0),
                null,
                LocalDateTime.of(2025, Month.MAY, 22, 15, 10)));

        tasks.add(new Task(13, "Backup computer", true, 3,
                LocalDateTime.of(2025, Month.JULY, 6, 18, 0),
                LocalDateTime.of(2025, Month.JULY, 1, 20, 0),
                LocalDateTime.of(2025, Month.MAY, 25, 16, 45)));

        tasks.add(new Task(14, "Plan weekend trip", false, 2,
                LocalDateTime.of(2025, Month.JULY, 12, 8, 0),
                null,
                LocalDateTime.of(2025, Month.MAY, 23, 8, 30)));

        tasks.add(new Task(15, "Refill prescriptions", true, 1,
                LocalDateTime.of(2025, Month.JUNE, 28, 9, 0),
                LocalDateTime.of(2025, Month.JUNE, 26, 10, 0),
                LocalDateTime.of(2025, Month.MAY, 26, 10, 15)));

    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }
}