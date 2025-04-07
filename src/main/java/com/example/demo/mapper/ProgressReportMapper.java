package com.example.demo.mapper;

import com.example.demo.model.ProgressReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ProgressReportMapper {
    /**
     * Find progress report by ID
     * @param reportId the report ID
     * @return the progress report if found
     */
    ProgressReport findById(@Param("reportId") String reportId);

    /**
     * Find all progress reports
     * @return list of all progress reports
     */
    List<ProgressReport> findAll();

    /**
     * Insert a new progress report
     * @param report the progress report to insert
     * @return number of rows affected
     */
    int insert(ProgressReport report);

    /**
     * Update an existing progress report
     * @param report the progress report to update
     * @return number of rows affected
     */
    int update(ProgressReport report);

    /**
     * Delete a progress report by ID
     * @param reportId the report ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("reportId") String reportId);

    /**
     * Find progress reports by student ID
     * @param studentId the student ID to search
     * @return list of matching progress reports
     */
    List<ProgressReport> findByStudentId(@Param("studentId") String studentId);

    /**
     * Find progress reports created after a specific time
     * @param time the time threshold
     * @return list of progress reports created after the specified time
     */
    List<ProgressReport> findByCreatedTimeAfter(@Param("time") LocalDateTime time);

    /**
     * Get the latest progress report for a student
     * @param studentId the student ID
     * @return the latest progress report for the student
     */
    ProgressReport findLatestByStudentId(@Param("studentId") String studentId);
}