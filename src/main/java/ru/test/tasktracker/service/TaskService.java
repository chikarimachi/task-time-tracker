package ru.test.tasktracker.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.test.tasktracker.dto.task.CreateTaskRequest;
import ru.test.tasktracker.dto.task.TaskResponse;
import ru.test.tasktracker.exception.NotFoundException;
import ru.test.tasktracker.mapper.TaskMapper;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.NEW);

        taskMapper.insert(task);
        return mapToResponse(task);
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return mapToResponse(getExistingTask(id));
    }

    @Transactional
    public TaskResponse updateStatus(Long taskId, TaskStatus status) {
        getExistingTask(taskId);
        taskMapper.updateStatus(taskId, status);
        return getTaskById(taskId);
    }

    @Transactional(readOnly = true)
    public Task getExistingTask(Long id) {
        Task task = taskMapper.findById(id);
        if (task == null) {
            throw new NotFoundException("Задача с id=" + id + " не найдена");
        }
        return task;
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }
}
