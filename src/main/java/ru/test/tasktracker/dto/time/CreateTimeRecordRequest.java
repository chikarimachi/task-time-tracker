package ru.test.tasktracker.dto.time;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateTimeRecordRequest {

    @NotNull(message = "employeeId обязателен")
    private Long employeeId;

    @NotNull(message = "taskId обязателен")
    private Long taskId;

    @NotNull(message = "startTime обязателен")
    private LocalDateTime startTime;

    @NotNull(message = "endTime обязателен")
    private LocalDateTime endTime;

    @NotBlank(message = "Описание работы обязательно")
    private String workDescription;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }
}
