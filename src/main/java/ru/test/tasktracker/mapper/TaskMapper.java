package ru.test.tasktracker.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;

@Mapper
public interface TaskMapper {
    void insert(Task task);

    Task findById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);
}
