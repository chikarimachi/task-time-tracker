package ru.test.tasktracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.test.tasktracker.dto.time.CreateTimeRecordRequest;
import ru.test.tasktracker.dto.time.TimeRecordResponse;
import ru.test.tasktracker.dto.time.TimeReportResponse;
import ru.test.tasktracker.service.TimeRecordService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/time-records")
@SecurityRequirement(name = "bearerAuth")
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    public TimeRecordController(TimeRecordService timeRecordService) {
        this.timeRecordService = timeRecordService;
    }

    @Operation(summary = "Создать запись о затраченном времени")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeRecordResponse createTimeRecord(@Valid @RequestBody CreateTimeRecordRequest request) {
        return timeRecordService.createTimeRecord(request);
    }

    @Operation(summary = "Получить время сотрудника за период")
    @GetMapping
    public TimeReportResponse getEmployeeTimeReport(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return timeRecordService.getEmployeeReport(employeeId, from, to);
    }
}
