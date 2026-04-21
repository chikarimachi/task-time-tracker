package ru.test.tasktracker.mapper;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.test.tasktracker.model.Task;
import ru.test.tasktracker.model.TaskStatus;
import ru.test.tasktracker.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TimeRecordMapperIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("tracker")
            .withUsername("tracker")
            .withPassword("tracker");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Test
    void shouldInsertAndReadTimeRecord() {
        Task task = new Task();
        task.setTitle("Интеграционный тест");
        task.setDescription("Проверка mapper слоя");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);

        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1001L);
        record.setTaskId(task.getId());
        record.setStartTime(LocalDateTime.of(2026, 4, 20, 10, 0));
        record.setEndTime(LocalDateTime.of(2026, 4, 20, 11, 30));
        record.setWorkDescription("Запись времени");
        timeRecordMapper.insert(record);

        List<TimeRecord> records = timeRecordMapper.findByEmployeeAndPeriod(
                1001L,
                LocalDateTime.of(2026, 4, 1, 0, 0),
                LocalDateTime.of(2026, 4, 30, 23, 59)
        );

        assertEquals(1, records.size());
        assertEquals("Запись времени", records.get(0).getWorkDescription());
        assertNotNull(records.get(0).getId());
    }
}
