package BreakableToy.Task;
import java.time.LocalDateTime;

public record Task(
        Integer id,
        String Name,
        boolean Done,
        Integer priority,
        LocalDateTime dueDate,
        LocalDateTime doneDate
) {
}
