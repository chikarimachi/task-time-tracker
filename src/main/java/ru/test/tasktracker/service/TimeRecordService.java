package ru.test.tasktracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.tasktracker.dto.time.CreateTimeRecordRequest;
import ru.test.tasktracker.dto.time.TimeRecordResponse;
import ru.test.tasktracker.dto.time.TimeReportResponse;
import ru.test.tasktracker.exception.BadRequestException;
import ru.test.tasktracker.mapper.TimeRecordMapper;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;
import ru.test.tasktracker.model.TimeRecord;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskService taskService;

    public TimeRecordService(TimeRecordMapper timeRecordMapper, TaskService taskService) {
        this.timeRecordMapper = timeRecordMapper;
        this.taskService = taskService;
    }

    @Transactional
    public TimeRecordResponse createTimeRecord(CreateTimeRecordRequest request) {
        validatePeriod(request.getStartTime(), request.getEndTime());

        Task task = taskService.getExistingTask(request.getTaskId());
        if (task.getStatus() == TaskStatus.DONE) {
            throw new BadRequestException("Нельзя добавлять время в задачу со статусом DONE");
        }

        TimeRecord record = new TimeRecord();
        record.setEmployeeId(request.getEmployeeId());
        record.setTaskId(request.getTaskId());
        record.setStartTime(request.getStartTime());
        record.setEndTime(request.getEndTime());
        record.setWorkDescription(request.getWorkDescription());

        timeRecordMapper.insert(record);
        return mapToResponse(record);
    }

    @Transactional(readOnly = true)
    public TimeReportResponse getEmployeeReport(Long employeeId, LocalDateTime from, LocalDateTime to) {
        validatePeriod(from, to);

        List<TimeRecordResponse> records = timeRecordMapper.findByEmployeeAndPeriod(employeeId, from, to)
                .stream()
                .map(this::mapToResponse)
                .toList();

        long totalMinutes = records.stream()
                .mapToLong(TimeRecordResponse::durationMinutes)
                .sum();

        return new TimeReportResponse(employeeId, totalMinutes, records);
    }

    private void validatePeriod(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new BadRequestException("Время окончания должно быть позже времени начала");
        }
    }

    private TimeRecordResponse mapToResponse(TimeRecord record) {
        long durationMinutes = Duration.between(record.getStartTime(), record.getEndTime()).toMinutes();
        return new TimeRecordResponse(
                record.getId(),
                record.getEmployeeId(),
                record.getTaskId(),
                record.getStartTime(),
                record.getEndTime(),
                record.getWorkDescription(),
                durationMinutes
        );
    }
}
