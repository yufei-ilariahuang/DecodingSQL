package com.example.demo.mapper;

import com.example.demo.model.ProblemAnswer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProblemAnswerMapper {
    /**
     * Find problem answer by problem ID
     * @param problemId the problem ID
     * @return the problem answer if found
     */
    ProblemAnswer findByProblemId(@Param("problemId") String problemId);

    /**
     * Find all problem answers
     * @return list of all problem answers
     */
    List<ProblemAnswer> findAll();

    /**
     * Insert a new problem answer
     * @param problemAnswer the problem answer to insert
     * @return number of rows affected
     */
    int insert(ProblemAnswer problemAnswer);

    /**
     * Update an existing problem answer
     * @param problemAnswer the problem answer to update
     * @return number of rows affected
     */
    int update(ProblemAnswer problemAnswer);

    /**
     * Delete a problem answer by problem ID
     * @param problemId the problem ID to delete
     * @return number of rows affected
     */
    int deleteByProblemId(@Param("problemId") String problemId);

    /**
     * Find problem answers by creator
     * @param createdBy the creator to search
     * @return list of matching problem answers
     */
    List<ProblemAnswer> findByCreator(@Param("createdBy") String createdBy);

    /**
     * Count total number of problem answers
     * @return total count of problem answers
     */
    int countAll();
}