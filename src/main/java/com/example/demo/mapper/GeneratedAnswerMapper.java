package com.example.demo.mapper;

import com.example.demo.model.GeneratedAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GeneratedAnswerMapper {
    /**
     * Find generated answer by problem ID
     * @param problemId the problem ID
     * @return the generated answer if found
     */
    GeneratedAnswer findByProblemId(@Param("problemId") String problemId);

    /**
     * Find all generated answers
     * @return list of all generated answers
     */
    List<GeneratedAnswer> findAll();

    /**
     * Insert a new generated answer
     * @param generatedAnswer the generated answer to insert
     * @return number of rows affected
     */
    int insert(GeneratedAnswer generatedAnswer);

    /**
     * Update an existing generated answer
     * @param generatedAnswer the generated answer to update
     * @return number of rows affected
     */
    int update(GeneratedAnswer generatedAnswer);

    /**
     * Delete a generated answer by problem ID
     * @param problemId the problem ID to delete
     * @return number of rows affected
     */
    int deleteByProblemId(@Param("problemId") String problemId);

    /**
     * Find generated answers by student ID
     * @param studentId the student ID to search
     * @return list of matching generated answers
     */
    List<GeneratedAnswer> findByStudentId(@Param("studentId") String studentId);

    /**
     * Find generated answers created after a specific time
     * @param time the time threshold
     * @return list of generated answers created after the specified time
     */
    List<GeneratedAnswer> findByGeneratedTimeAfter(@Param("time") LocalDateTime time);
}