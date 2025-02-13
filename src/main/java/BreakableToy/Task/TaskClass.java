package BreakableToy.Task;
import java.time.LocalDateTime;

public record TaskClass(
        Integer id,
        String Name,
        boolean Done,
        Integer priority,
        LocalDateTime dueDate
) {
}
