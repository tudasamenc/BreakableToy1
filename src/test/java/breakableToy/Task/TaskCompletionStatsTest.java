package breakableToy.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@SpringBootTest
class TaskCompletionStatsTest {
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // Create a mock using Mockito
        ChatClient.Builder mockBuilder = mock(ChatClient.Builder.class);
        taskRepository = new TaskRepository(mockBuilder);

        // Add test tasks with known completion times
        // Priority 1 task - 24 hours completion time
        taskRepository.create(new Task(
                1,
                "Priority 1 Task",
                true,
                1,
                LocalDateTime.of(2025, Month.JANUARY, 2, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 2, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0)
        ));

        // Priority 2 task - 48 hours completion time
        taskRepository.create(new Task(
                2,
                "Priority 2 Task",
                true,
                2,
                LocalDateTime.of(2025, Month.JANUARY, 3, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 3, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0)
        ));

        // Priority 3 task - 72 hours completion time
        taskRepository.create(new Task(
                3,
                "Priority 3 Task",
                true,
                3,
                LocalDateTime.of(2025, Month.JANUARY, 4, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 4, 12, 0),
                LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0)
        ));

        // Incomplete task - should not affect calculations
        taskRepository.create(new Task(
                4,
                "Incomplete Task",
                false,
                1,
                LocalDateTime.of(2025, Month.JANUARY, 5, 12, 0),
                null,
                LocalDateTime.of(2025, Month.JANUARY, 1, 12, 0)
        ));
    }

    @Test
    void calculateAverageCompletionTimes_ShouldCalculateCorrectAverages() {
        TaskCompletionStats stats = taskRepository.calculateAverageCompletionTimes();

        assertEquals(48.0, stats.overallAverageHours(), 0.1, "Overall average should be 48 hours");
        assertEquals(24.0, stats.priority1AverageHours(), 0.1, "Priority 1 average should be 24 hours");
        assertEquals(48.0, stats.priority2AverageHours(), 0.1, "Priority 2 average should be 48 hours");
        assertEquals(72.0, stats.priority3AverageHours(), 0.1, "Priority 3 average should be 72 hours");
    }

    @Test
    void calculateAverageCompletionTimes_WithNoCompletedTasks_ShouldReturnZeros() {
        // Create a new repository with a mock builder
        ChatClient.Builder emptyMockBuilder = mock(ChatClient.Builder.class);
        TaskRepository emptyRepo = new TaskRepository(emptyMockBuilder);

        TaskCompletionStats stats = emptyRepo.calculateAverageCompletionTimes();

        assertEquals(0.0, stats.overallAverageHours(), "Overall average should be 0 for empty repository");
        assertEquals(0.0, stats.priority1AverageHours(), "Priority 1 average should be 0 for empty repository");
        assertEquals(0.0, stats.priority2AverageHours(), "Priority 2 average should be 0 for empty repository");
        assertEquals(0.0, stats.priority3AverageHours(), "Priority 3 average should be 0 for empty repository");
    }
}