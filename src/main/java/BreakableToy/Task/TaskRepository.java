package BreakableToy.Task;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {
    private List<TaskClass> tasks = new ArrayList<>();

    List<TaskClass> findAll()
    {
        return tasks;
    }

    Optional<TaskClass> findById(Integer id){
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst();
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
        tasks.add(new TaskClass(1,"Uno",false,1, LocalDateTime.now()));
        tasks.add(new TaskClass(2,"dos",false,2, LocalDateTime.now()));
    }
}
