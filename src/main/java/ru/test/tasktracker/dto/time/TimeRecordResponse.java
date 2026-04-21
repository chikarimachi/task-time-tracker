package ru.test.tasktracker.dto.time;

import java.time.LocalDateTime;

public record TimeRecordResponse(
        Long id,
        Long employeeId,
        Long taskId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String workDescription,
        long durationMinutes
) {
}
