package ru.test.tasktracker.dto.task;

import ru.test.tasktracker.model.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status
) {
}
