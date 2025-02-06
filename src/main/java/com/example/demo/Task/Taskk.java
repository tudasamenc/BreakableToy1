package com.example.demo.Task;
import java.time.LocalDateTime;

public record Taskk(
        Integer id,
        String Name,
        boolean Done,
        Integer priority,
        LocalDateTime dueDate
) {
}
