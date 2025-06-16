package breakableToy.Task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    void getCompletionStats_ShouldReturnStats() throws Exception {
        // Prepare test data
        TaskCompletionStats mockStats = new TaskCompletionStats(48.0, 24.0, 48.0, 72.0);

        // Mock repository response
        when(taskRepository.calculateAverageCompletionTimes()).thenReturn(mockStats);

        // Perform GET request and verify response
        mockMvc.perform(get("/api/tasks/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallAverageHours").value(48.0))
                .andExpect(jsonPath("$.priority1AverageHours").value(24.0))
                .andExpect(jsonPath("$.priority2AverageHours").value(48.0))
                .andExpect(jsonPath("$.priority3AverageHours").value(72.0));
    }
}