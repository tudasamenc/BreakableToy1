package breakableToy.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepo;

    public TaskController(TaskRepository tskRepo) {
        this.taskRepo = tskRepo;
    }

    // Return all tasks
    @GetMapping("")
    public List<Task> findAll() {
        return taskRepo.findAll();
    }

    // Find by ID
    @GetMapping("/{id}")
    public Task findById(@PathVariable Integer id) {
        return taskRepo.findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }

    // Filtered search
    @GetMapping("/filter")
    public List<Task> findFilteredTasks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Boolean done
    ) {
        boolean N = (name == null);
        boolean P = (priority == null);
        boolean D = (done == null);
        return taskRepo.findAllMasked(
                P, priority != null ? priority : 1,
                D, done != null ? done : false,
                N, name != null ? name : ""
        );
    }

    // Paginated + sorted
    @GetMapping("/paginated")
    public List<Task> getPaginatedTasks(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int sortvar,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "false") boolean filterDone,
            @RequestParam(defaultValue = "false") boolean done,
            @RequestParam(defaultValue = "false") boolean filterPriority,
            @RequestParam(defaultValue = "3") int priority
    ) {
        boolean sortType=false;
        boolean enablesort=false;
        sortType = switch (sortvar) {
            case 0 -> {
                enablesort = false;
                yield true;
            }
            case 1 -> {
                enablesort = true;
                yield true;
            }
            case 2 -> {
                enablesort = true;
                yield false;
            }
            default -> sortType;
        };
        return taskRepo.findSortedAndPaginated(enablesort && sortType,asc,enablesort && !sortType,asc,page, size,query,filterDone,done,filterPriority,priority);
    }

    // Create a task
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@RequestBody Task task) {
        int s = taskRepo.size();
        int id = taskRepo.findElementAt(s - 1).id() + 1;
        Task taskWithId = new Task(
                id,
                task.name(),
                task.done(),
                task.priority(),
                task.dueDate(),
                task.doneDate(),
                task.creationDate()
        );
        taskRepo.create(taskWithId);
    }

    // Update task by ID
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update/{id}")
    public void update(@RequestBody Task task, @PathVariable Integer id) {
        taskRepo.update(task, id);
    }

    // Delete task by ID
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        taskRepo.delete(id);
    }

    // Set done status
    @PatchMapping("/done/{id}")
    public Task setDone(@PathVariable Integer id, @RequestParam boolean done) {
        return taskRepo.setDone(id, done)
                .orElseThrow(TaskNotFoundException::new);
    }

    // Count tasks
    @GetMapping("/count")
    public int getTaskCount() {
        return taskRepo.size();
    }

    // Test endpoint
    @GetMapping("/hello")
    public String home() {
        return "Hello world";
    }
}
