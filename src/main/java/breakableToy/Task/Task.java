package breakableToy.Task;
import com.fasterxml.jackson.annotation.JsonTypeId;

import java.time.LocalDateTime;

public record Task(
        Integer id,
        String name,
        boolean done,
        Integer priority,
        LocalDateTime dueDate,
        LocalDateTime doneDate
) {
}
