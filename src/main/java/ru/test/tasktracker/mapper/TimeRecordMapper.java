package ru.test.tasktracker.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import ru.test.tasktracker.model.TimeRecord;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {
    void insert(TimeRecord timeRecord);

    List<TimeRecord> findByEmployeeAndPeriod(@Param("employeeId") Long employeeId,
                                             @Param("from") LocalDateTime from,
                                             @Param("to") LocalDateTime to);
}
