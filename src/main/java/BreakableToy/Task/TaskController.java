package BreakableToy.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepo;

    public TaskController(TaskRepository tskRepo){
        this.taskRepo=tskRepo;
    }
    @GetMapping("")
    List<TaskClass> findAll(){
        return taskRepo.findAll();
    }

    @GetMapping("/{id}")
    TaskClass findById(@PathVariable Integer id){
        Optional<TaskClass> task = taskRepo.findById(id);
        if (task.isEmpty()){
            throw new TaskNotFoundException();
        }
        return task.get();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody TaskClass task){
        taskRepo.create(task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@RequestBody TaskClass task, @PathVariable Integer id){
        taskRepo.update(task,id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id){
        taskRepo.delete(id);
    }

    @GetMapping("/hello")
    String home(){
        return "Hello world";
    }
}
