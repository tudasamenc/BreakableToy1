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

    public TaskController(TaskRepository tskRepo){
        this.taskRepo=tskRepo;
    }
    @GetMapping("")
    List<Task> findAll(){
        return taskRepo.findAll();
    }


    @GetMapping("/{id}")
    Task findById(@PathVariable Integer id){
        Optional<Task> task = taskRepo.findById(id);
        if (task.isEmpty()){
            throw new TaskNotFoundException();
        }
        return task.get();
    }

    @GetMapping("/find/{name}.{priorityString}.{doneString}/")
    List<Task> findAllByMasked(@PathVariable String name, @PathVariable String priorityString, @PathVariable String doneString){
        boolean P,D,N,done2;
        int priority,done;

        if(priorityString.equals(" ")){priority=1;}
        else{priority=Integer.parseInt(priorityString);}

        if(doneString.equals(" ")){done=1;}
        else{done=Integer.parseInt(doneString);}

        N=name.equals(" ");
        P=priorityString.equals(" ");
        D=doneString.equals(" ");
        done2=done==1;
        return taskRepo.findAllMasked(P,priority,D,done2,N,name);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@RequestBody Task task){
        int s = taskRepo.size();
        int id = taskRepo.findElementAt(s-1).id()+1;
        Task tasknewid= new Task(id,task.name(), task.done(), task.priority(), task.dueDate(),task.doneDate());
        taskRepo.create(tasknewid);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/update/{id}")
    void update(@RequestBody Task task, @PathVariable Integer id){
        taskRepo.update(task,id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    void delete(@PathVariable Integer id){
        taskRepo.delete(id);
    }

    @GetMapping("/hello")
    String home(){
        return "Hello world";
    }
}
