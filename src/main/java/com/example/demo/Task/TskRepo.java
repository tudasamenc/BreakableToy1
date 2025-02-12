package com.example.demo.Task;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TskRepo {
    private List<Taskk> tasks = new ArrayList<>();

    List<Taskk> findAll()
    {
        return tasks;
    }

    Optional<Taskk> findById(Integer id){
        return tasks.stream()
                .filter(taskk -> taskk.id() == id)
                .findFirst();
    }

    void create(Taskk task){
        tasks.add(task);
    }
    @PostConstruct
    private void init(){
        tasks.add(new Taskk(1,"Uno",false,1, LocalDateTime.now()));
        tasks.add(new Taskk(2,"dos",false,2, LocalDateTime.now()));
    }
}
