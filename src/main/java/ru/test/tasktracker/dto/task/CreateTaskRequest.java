package ru.test.tasktracker.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskRequest {

    @NotBlank(message = "Название задачи обязательно")
    @Size(max = 255, message = "Название задачи не должно быть длиннее 255 символов")
    private String title;

    @Size(max = 2000, message = "Описание не должно быть длиннее 2000 символов")
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
