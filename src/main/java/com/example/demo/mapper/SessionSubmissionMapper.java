package com.example.demo.mapper;

import com.example.demo.model.SessionSubmission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SessionSubmissionMapper {
    /**
     * Find session submission by ID
     * @param submissionId the submission ID
     * @return the submission if found
     */
    SessionSubmission findById(@Param("submissionId") String submissionId);

    /**
     * Find all session submissions
     * @return list of all session submissions
     */
    List<SessionSubmission> findAll();

    /**
     * Insert a new session submission
     * @param submission the submission to insert
     * @return number of rows affected
     */
    int insert(SessionSubmission submission);

    /**
     * Update an existing session submission
     * @param submission the submission to update
     * @return number of rows affected
     */
    int update(SessionSubmission submission);

    /**
     * Delete a session submission by ID
     * @param submissionId the submission ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("submissionId") String submissionId);

    /**
     * Find session submissions by student ID
     * @param studentId the student ID to search
     * @return list of matching session submissions
     */
    List<SessionSubmission> findByStudentId(@Param("studentId") String studentId);

    /**
     * Find session submissions by problem ID
     * @param problemId the problem ID to search
     * @return list of matching session submissions
     */
    List<SessionSubmission> findByProblemId(@Param("problemId") String problemId);

    /**
     * Find session submissions by grade range
     * @param minGrade the minimum grade (inclusive)
     * @param maxGrade the maximum grade (inclusive)
     * @return list of matching session submissions
     */
    List<SessionSubmission> findByGradeRange(
            @Param("minGrade") int minGrade,
            @Param("maxGrade") int maxGrade
    );

    /**
     * Get average grade for a student
     * @param studentId the student ID
     * @return the average grade for the student
     */
    Double getAverageGradeByStudentId(@Param("studentId") String studentId);

    /**
     * Get maximum grade for a student
     * @param studentId the student ID
     * @return the maximum grade for the student
     */
    Integer getMaxGradeByStudentId(@Param("studentId") String studentId);

    List<SessionSubmission> findByStudentIdAndProblemId(@Param("studentId") String studentId,
                                                        @Param("problemId") String problemId);
}