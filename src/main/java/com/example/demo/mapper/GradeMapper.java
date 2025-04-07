package com.example.demo.mapper;

import com.example.demo.model.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GradeMapper {
    /**
     * Find grade by session ID and created time
     * @param sessionId the session ID
     * @param createdTime the created time
     * @return the grade if found
     */
    Grade findBySessionIdAndCreatedTime(
            @Param("sessionId") String sessionId,
            @Param("createdTime") LocalDateTime createdTime
    );

    /**
     * Find all grades
     * @return list of all grades
     */
    List<Grade> findAll();

    /**
     * Insert a new grade
     * @param grade the grade to insert
     * @return number of rows affected
     */
    int insert(Grade grade);

    /**
     * Update an existing grade
     * @param grade the grade to update
     * @return number of rows affected
     */
    int update(Grade grade);

    /**
     * Delete a grade by session ID and created time
     * @param sessionId the session ID
     * @param createdTime the created time
     * @return number of rows affected
     */
    int deleteBySessionIdAndCreatedTime(
            @Param("sessionId") String sessionId,
            @Param("createdTime") LocalDateTime createdTime
    );

    /**
     * Find grades by session ID
     * @param sessionId the session ID to search
     * @return list of matching grades
     */
    List<Grade> findBySessionId(@Param("sessionId") String sessionId);

    /**
     * Find grades created after a specific time
     * @param time the time threshold
     * @return list of grades created after the specified time
     */
    List<Grade> findByCreatedTimeAfter(@Param("time") LocalDateTime time);

    /**
     * Count total number of grades
     * @return total count of grades
     */
    int countAll();
}