package ru.test.tasktracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.test.tasktracker.dto.task.CreateTaskRequest;
import ru.test.tasktracker.exception.NotFoundException;
import ru.test.tasktracker.mapper.TaskMapper;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTaskWithStatusNew() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Новая задача");
        request.setDescription("Описание");

        doAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(10L);
            return null;
        }).when(taskMapper).insert(any(Task.class));

        var response = taskService.createTask(request);

        assertEquals(10L, response.id());
        assertEquals(TaskStatus.NEW, response.status());

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskMapper).insert(captor.capture());
        assertEquals("Новая задача", captor.getValue().getTitle());
        assertEquals(TaskStatus.NEW, captor.getValue().getStatus());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskMapper.findById(999L)).thenReturn(null);

        NotFoundException ex = assertThrows(NotFoundException.class, () -> taskService.getTaskById(999L));
        assertTrue(ex.getMessage().contains("не найдена"));
    }
}
