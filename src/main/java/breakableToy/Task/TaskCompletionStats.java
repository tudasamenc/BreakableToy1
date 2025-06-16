package breakableToy.Task;

public record TaskCompletionStats(
        double overallAverageHours,
        double priority1AverageHours,
        double priority2AverageHours,
        double priority3AverageHours
) {}
