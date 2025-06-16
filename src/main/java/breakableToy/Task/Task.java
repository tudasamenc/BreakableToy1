package breakableToy.Task;

import java.time.LocalDateTime;

/**
 * The Task record represents a task with various metadata and status information.
 *
 * This immutable data structure is designed to encapsulate details and properties
 * of a task such as its unique identifier, name, status, priority, and timestamps
 * for key events like creation, completion, and due dates.
 *
 * Fields:
 * - id: Unique identifier for the task.
 * - name: A brief description or title of the task.
 * - done: Boolean flag indicating whether the task is completed.
 * - priority: Numeric priority associated with the task.
 * - dueDate: The date and time by which the task is expected to be completed.
 * - doneDate: The date and time when the task was marked as completed.
 * - creationDate: The date and time when the task was created.
 */
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
