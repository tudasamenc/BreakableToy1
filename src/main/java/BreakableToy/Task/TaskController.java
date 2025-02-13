package BreakableToy.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository tskRepo;

    public TaskController(TaskRepository tskRepo){
        this.tskRepo=tskRepo;
    }
    @GetMapping("")
    List<TaskClass> findAll(){
        return tskRepo.findAll();
    }

    @GetMapping("/{id}")
    TaskClass findById(@PathVariable Integer id){
        Optional<TaskClass> task = tskRepo.findById(id);
        if (task.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return task.get();
    }

    void create(@RequestBody TaskClass task){
        tskRepo.create(task);
    }

    @GetMapping("/hello")
    String home(){
        return "Hello world";
    }
}
