package com.example.demo.mapper;

import com.example.demo.model.TrainingSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface TrainingSessionMapper {
    /**
     * Find training session by ID
     * @param sessionId the session ID
     * @return the training session if found
     */
    TrainingSession findById(@Param("sessionId") String sessionId);

    /**
     * Find all training sessions
     * @return list of all training sessions
     */
    List<TrainingSession> findAll();

    /**
     * Insert a new training session
     * @param session the training session to insert
     * @return number of rows affected
     */
    int insert(TrainingSession session);

    /**
     * Update an existing training session
     * @param session the training session to update
     * @return number of rows affected
     */
    int update(TrainingSession session);

    /**
     * Delete a training session by ID
     * @param sessionId the session ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("sessionId") String sessionId);

    /**
     * Find training sessions by student ID (searches in student_ids field)
     * @param studentId the student ID to search for
     * @return list of matching training sessions
     */
    List<TrainingSession> findByStudentId(@Param("studentId") String studentId);

    /**
     * Find training sessions by problem ID (searches in problem_ids field)
     * @param problemId the problem ID to search for
     * @return list of matching training sessions
     */
    List<TrainingSession> findByProblemId(@Param("problemId") String problemId);

    /**
     * Find training sessions created on a specific date
     * @param date the date to search for
     * @return list of matching training sessions
     */
    List<TrainingSession> findByCreatedDate(@Param("date") LocalDate date);
}