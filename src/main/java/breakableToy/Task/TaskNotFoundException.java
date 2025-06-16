package breakableToy.Task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class representing a scenario where a task is not found.
 * This exception is a custom runtime exception and is marked with a
 * {@link ResponseStatus} annotation
 * to respond with an HTTP 404 (Not Found) status when thrown.
 *
 * It is used primarily in the context of the Task management system
 * to signal that a requested task does not exist in the data repository.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(){
        super("Task Not Found");
    }
}
