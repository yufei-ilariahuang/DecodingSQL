package com.example.demo.mapper;

import com.example.demo.model.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * Find students with dynamic filtering options
     */
    List<Student> findStudents(Map<String, Object> params);

    /**
     * Get comprehensive statistics for a specific student
     */
    Map<String, Object> getStudentStatistics(@Param("studentId") Long studentId);
}