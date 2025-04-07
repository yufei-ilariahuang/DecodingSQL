package com.example.demo.mapper;

import com.example.demo.model.SqlProblem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SqlProblemMapper {
    /**
     * Find SQL problem by ID
     * @param problemId the problem ID
     * @return the problem if found
     */
    SqlProblem findById(@Param("problemId") String problemId);

    /**
     * Find all SQL problems
     * @return list of all SQL problems
     */
    List<SqlProblem> findAll();

    /**
     * Insert a new SQL problem
     * @param problem the SQL problem to insert
     * @return number of rows affected
     */
    int insert(SqlProblem problem);

    /**
     * Update an existing SQL problem
     * @param problem the SQL problem to update
     * @return number of rows affected
     */
    int update(SqlProblem problem);

    /**
     * Delete a SQL problem by ID
     * @param problemId the problem ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("problemId") String problemId);

    /**
     * Find SQL problems by difficulty level
     * @param difficultyLevel the difficulty level to search
     * @return list of matching SQL problems
     */
    List<SqlProblem> findByDifficultyLevel(@Param("difficultyLevel") String difficultyLevel);

    /**
     * Find SQL problems by creator
     * @param createdBy the creator to search
     * @return list of matching SQL problems
     */
    List<SqlProblem> findByCreator(@Param("createdBy") String createdBy);

    /**
     * Find SQL problems by title (partial match)
     * @param title the title to search
     * @return list of matching SQL problems
     */
    List<SqlProblem> findByTitle(@Param("title") String title);
}