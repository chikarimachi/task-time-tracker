package ru.test.tasktracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.tasktracker.dto.time.CreateTimeRecordRequest;
import ru.test.tasktracker.exception.BadRequestException;
import ru.test.tasktracker.mapper.TimeRecordMapper;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;
import ru.test.tasktracker.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordMapper timeRecordMapper;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TimeRecordService timeRecordService;

    @Test
    void shouldThrowWhenEndTimeNotAfterStartTime() {
        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(1001L);
        request.setTaskId(1L);
        request.setStartTime(LocalDateTime.of(2026, 4, 20, 10, 0));
        request.setEndTime(LocalDateTime.of(2026, 4, 20, 9, 0));
        request.setWorkDescription("Работа");

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> timeRecordService.createTimeRecord(request));

        assertTrue(ex.getMessage().contains("окончания"));
    }

    @Test
    void shouldThrowWhenTaskIsDone() {
        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(1001L);
        request.setTaskId(1L);
        request.setStartTime(LocalDateTime.of(2026, 4, 20, 10, 0));
        request.setEndTime(LocalDateTime.of(2026, 4, 20, 11, 0));
        request.setWorkDescription("Работа");

        Task task = new Task();
        task.setId(1L);
        task.setStatus(TaskStatus.DONE);

        when(taskService.getExistingTask(1L)).thenReturn(task);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> timeRecordService.createTimeRecord(request));

        assertTrue(ex.getMessage().contains("DONE"));
    }

    @Test
    void shouldBuildEmployeeReport() {
        TimeRecord record = new TimeRecord();
        record.setId(1L);
        record.setEmployeeId(1001L);
        record.setTaskId(2L);
        record.setStartTime(LocalDateTime.of(2026, 4, 20, 10, 0));
        record.setEndTime(LocalDateTime.of(2026, 4, 20, 12, 30));
        record.setWorkDescription("Анализ");

        when(timeRecordMapper.findByEmployeeAndPeriod(anyLong(), any(), any()))
                .thenReturn(List.of(record));

        var response = timeRecordService.getEmployeeReport(
                1001L,
                LocalDateTime.of(2026, 4, 1, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59)
        );

        assertEquals(150, response.totalMinutes());
        assertEquals(1, response.records().size());
    }
}
