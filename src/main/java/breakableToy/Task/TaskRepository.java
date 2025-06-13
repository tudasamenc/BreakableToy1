package breakableToy.Task;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private List<Task> tasks = new ArrayList<>();

    //Find every task ad show it
    List<Task> findAll()
    {
        return tasks;
    }
    
    
    //Find all tasks that are done
    List<Task> findAllByDone(boolean done) {
        return tasks.stream()
                .filter(task -> task.done() == done)
                .toList();
    }
    int size() {
        return tasks.size();
    }

    Task findElementAt(int i){
        return tasks.get(i);
    }


    List<Task> findAllByPriority(Integer priority){
        return tasks.stream()
                .filter(task -> task.priority().equals(priority))
                .toList();
    }


    List<Task> findAllMasked(boolean P, Integer priority, boolean D, boolean done, boolean N, String name){
        return tasks.stream()
                .filter(task -> P||task.priority().equals(priority))
                .filter(task -> D||task.done() == done)
                .filter(task -> N||task.name().contains(name))
                .toList();
    }

    List<Task> sortByPriority() {
        return tasks.stream()
                .sorted(Comparator.comparingInt(Task::priority))
                .toList();
    }

    Optional<Task> findById(Integer id){
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst();
    }

    Optional<Task> setDone(Integer id, boolean done) {
        Optional<Task> taskOpt = findById(id);
        taskOpt.ifPresent(task -> {
            Task updatedTask = new Task(task.id(), task.name(), done, task.priority(), task.dueDate(),
                    done ? LocalDateTime.now() : null,task.creationDate());
            tasks.set(tasks.indexOf(task), updatedTask);
        });
        return taskOpt;
    }

    void create(Task task){
        tasks.add(task);
    }

    void update(Task task, Integer id){
        Optional<Task> existingTask = findById(id);
        if(existingTask.isPresent()){
            tasks.set(tasks.indexOf(existingTask.get()),task);
        }
    }

    void delete(Integer id){
        tasks.removeIf(task -> task.id().equals(id));
    }


    @PostConstruct
    private void init(){
        tasks.add(new Task(1,"Tarea Uno",false,1, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(2,"Tarea Dos",false,2, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(3,"Tarea Tres",false,3, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(4,"Tarea Cuatro",false,2, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));;
        tasks.add(new Task(5,"Tarea Cinco",false,1, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(6,"Tarea Seis",false,2, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(7,"Tarea Siete",true,2, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(8,"Tarea Ocho",false,3, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(9,"Tarea Nueve",false,1, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(10,"Tarea Diez",false,2, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));
        tasks.add(new Task(11,"Tarea Once",false,1, LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()));


    }
}
