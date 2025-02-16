package BreakableToy.Task;
import com.sun.source.util.TaskListener;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private List<TaskClass> tasks = new ArrayList<>();

    //Find every task ad show it
    List<TaskClass> findAll()
    {
        return tasks;
    }
    
    
    //Find all tasks that are done
    List<TaskClass> findAllByDone(boolean done) {
        return tasks.stream()
                .filter(task -> task.Done() == done)
                .toList();
    }


    List<TaskClass> findAllByPriority(Integer priority){
        return tasks.stream()
                .filter(task -> task.priority().equals(priority))
                .toList();
    }

    List<TaskClass> sortByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(TaskClass::priority))
                .toList();
    }

    Optional<TaskClass> findById(Integer id){
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst();
    }

    Optional<TaskClass> SetDone(Integer id, boolean done) {
        Optional<TaskClass> taskOpt = findById(id);
        taskOpt.ifPresent(task -> {
            TaskClass updatedTask = new TaskClass(task.id(), task.Name(), done, task.priority(), task.dueDate(),
                    done ? LocalDateTime.now() : null);
            tasks.set(tasks.indexOf(task), updatedTask);
        });
        return taskOpt;
    }

    void create(TaskClass task){
        tasks.add(task);
    }

    void update(TaskClass task, Integer id){
        Optional<TaskClass> existingTask = findById(id);
        if(existingTask.isPresent()){
            tasks.set(tasks.indexOf(existingTask.get()),task);
        }
    }

    void delete(Integer id){
        tasks.removeIf(task -> task.id().equals(id));
    }


    @PostConstruct
    private void init(){
        tasks.add(new TaskClass(1,"Tarea Uno",false,1, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(2,"Tarea Dos",false,2, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(3,"Tarea Tres",false,3, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(4,"Tarea Cuatro",false,2, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(5,"Tarea Cinco",false,1, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(6,"Tarea Seis",false,2, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(7,"Tarea Siete",false,2, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(8,"Tarea Ocho",false,3, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(9,"Tarea Nueve",false,1, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(10,"Tarea Diez",false,2, LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new TaskClass(11,"Tarea Once",false,1, LocalDateTime.now(),LocalDateTime.now()));

    }
}
