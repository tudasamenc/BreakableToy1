package com.example.demo.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tasks")
public class TskController {
    private final TskRepo tskRepo;

    public TskController(TskRepo tskRepo){
        this.tskRepo=tskRepo;
    }
    @GetMapping("")
    List<Taskk> findAll(){
        return tskRepo.findAll();
    }

    @GetMapping("/{id}")
    Taskk findById(@PathVariable Integer id){
        Optional<Taskk> task = tskRepo.findById(id);
        if (task.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return task.get();
    }

    void create(@RequestBody Taskk task){
        tskRepo.create(task);
    }

    @GetMapping("/hello")
    String home(){
        return "Hello world";
    }
}
