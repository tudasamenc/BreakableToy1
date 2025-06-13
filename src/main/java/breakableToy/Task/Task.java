package breakableToy.Task;

import java.time.LocalDateTime;

public record Task(
        Integer id,
        String name,
        boolean done,
        Integer priority,
        LocalDateTime dueDate,
        LocalDateTime doneDate,
        LocalDateTime creationDate
) {
}
