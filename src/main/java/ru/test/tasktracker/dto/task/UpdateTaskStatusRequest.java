package ru.test.tasktracker.dto.task;

import jakarta.validation.constraints.NotNull;
import ru.test.tasktracker.model.TaskStatus;

public class UpdateTaskStatusRequest {

    @NotNull(message = "Статус обязателен")
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
