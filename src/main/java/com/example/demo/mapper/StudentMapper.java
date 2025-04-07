package com.example.demo.mapper;

import com.example.demo.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    /**
     * Find student by ID
     * @param studentId the student ID
     * @return the student if found
     */
    Student findById(@Param("studentId") String studentId);

    /**
     * Find all students
     * @return list of all students
     */
    List<Student> findAll();

    /**
     * Insert a new student
     * @param student the student to insert
     * @return number of rows affected
     */
    int insert(Student student);

    /**
     * Update an existing student
     * @param student the student to update
     * @return number of rows affected
     */
    int update(Student student);

    /**
     * Delete a student by ID
     * @param studentId the student ID to delete
     * @return number of rows affected
     */
    int deleteById(@Param("studentId") String studentId);

    /**
     * Find students by username
     * @param username the username to search
     * @return list of matching students
     */
    List<Student> findByUsername(@Param("username") String username);

    /**
     * Find student by email
     * @param email the email to search
     * @return the student if found
     */
    Student findByEmail(@Param("email") String email);
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
    /**
     * Find students with dynamic filtering options
     */
    List<Student> findStudents(Map<String, Object> params);

    /**
     * Get comprehensive statistics for a specific student
     */
    Map<String, Object> getStudentStatistics(@Param("studentId") String studentId);
}