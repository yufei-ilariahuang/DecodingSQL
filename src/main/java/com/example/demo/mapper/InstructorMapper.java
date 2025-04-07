package com.example.demo.mapper;

import com.example.demo.model.Instructor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InstructorMapper {
    /**
     * Find instructor by ID
     * @param instructorId the instructor ID
     * @return the instructor if found
     */
    Instructor findById(@Param("instructorId") String instructorId);

    /**
     * Find all instructors
     * @return list of all instructors
     */
    List<Instructor> findAll();

    /**
     * Insert a new instructor
     * @param instructor the instructor to insert
     * @return number of rows affected
     */
    int insert(Instructor instructor);

    /**
     * Update an existing instructor
     * @param instructor the instructor to update
     * @return number of rows affected
     */
    int update(Instructor instructor);

    /**
     * Delete an instructor by ID
     * @param instructorId the instructor ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("instructorId") String instructorId);

    /**
     * Find instructor by username
     * @param username the username to search
     * @return the instructor if found
     */
    Instructor findByUsername(@Param("username") String username);

    /**
     * Find instructor by email
     * @param email the email to search
     * @return the instructor if found
     */
    Instructor findByEmail(@Param("email") String email);

    /**
     * Check if a username already exists
     * @param username the username to check
     * @return true if exists, false otherwise
     */
    boolean existsByUsername(@Param("username") String username);

    /**
     * Check if an email already exists
     * @param email the email to check
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(@Param("email") String email);
}