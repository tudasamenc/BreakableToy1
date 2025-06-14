package breakableToy.Task;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final List<Task> tasks = new ArrayList<>();

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
        existingTask.ifPresent(value -> tasks.set(tasks.indexOf(value), task));
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

    //  Combined sorting and pagination
    public List<Task> findSortedAndPaginated(
            boolean sortByPriority,
            boolean priorityAsc,
            boolean sortByDone,
            boolean doneAsc,
            int page,
            int size,
            String search,
            boolean filterByDoneEnabled,
            boolean doneFilterValue,
            boolean filterByPriorityEnabled,
            int priorityFilterValue
    ) {
        List<Task> sorted = findSorted(sortByPriority, priorityAsc, sortByDone, doneAsc);

        // Filter by search string in "name"
        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            sorted = sorted.stream()
                    .filter(task -> task.name() != null && task.name().toLowerCase().contains(searchLower))
                    .toList();
        }

        // Filter by done status if enabled
        if (filterByDoneEnabled) {
            sorted = sorted.stream()
                    .filter(task -> task.done() == doneFilterValue)
                    .toList();
        }

        // Filter by priority if enabled
        if (filterByPriorityEnabled) {
            sorted = sorted.stream()
                    .filter(task -> task.priority() != null && task.priority().equals(priorityFilterValue))
                    .toList();
        }

        // Pagination
        int fromIndex = page * size;
        if (fromIndex >= sorted.size()) {
            return List.of();
        }

        int toIndex = Math.min(fromIndex + size, sorted.size());
        return sorted.subList(fromIndex, toIndex);
    }

    // Initial data
    @PostConstruct
    private void init() {
        tasks.add(new Task(1, "Tarea Uno", false, 1, now(), now(), now()));
        tasks.add(new Task(2, "Tarea Dos", false, 2, now(), now(), now()));
        tasks.add(new Task(3, "Tarea Tres", false, 3, now(), now(), now()));
        tasks.add(new Task(4, "Tarea Cuatro", false, 2, now(), now(), now()));
        tasks.add(new Task(5, "Tarea Cinco", false, 1, now(), now(), now()));
        tasks.add(new Task(6, "Tarea Seis", false, 2, now(), now(), now()));
        tasks.add(new Task(7, "Tarea Siete", true, 2, now(), now(), now()));
        tasks.add(new Task(8, "Tarea Ocho", false, 3, now(), now(), now()));
        tasks.add(new Task(9, "Tarea Nueve", false, 1, now(), now(), now()));
        tasks.add(new Task(10, "Tarea Diez", false, 2, now(), now(), now()));
        tasks.add(new Task(11, "Tarea Once", false, 1, now(), now(), now()));
        tasks.add(new Task(12, "Tarea Doce", false, 2, now(), now(), now()));
        tasks.add(new Task(13, "Tarea Trece", false, 3, now(), now(), now()));
    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }
}
