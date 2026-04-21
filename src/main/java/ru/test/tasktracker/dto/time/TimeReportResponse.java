package ru.test.tasktracker.dto.time;

import java.util.List;

public record TimeReportResponse(
        Long employeeId,
        long totalMinutes,
        List<TimeRecordResponse> records
) {
}
