package com.example.demo.mapper;

import com.example.demo.model.CourseMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CourseMetadataMapper {
    CourseMetadata findById(String sessionId);
    List<CourseMetadata> findAll();
    List<CourseMetadata> findByInstructorId(String instructorId);
    int insert(CourseMetadata courseMetadata);
    int update(CourseMetadata courseMetadata);
    int deleteById(String sessionId);

    // Methods for stored procedures
    List<Map<String, Object>> getStudentCourses(@Param("studentId") String studentId);
    List<Map<String, Object>> getInstructorCourses(@Param("instructorId") String instructorId);

    // Additional helper methods
    List<Map<String, Object>> findSessionsByStudentId(@Param("studentId") String studentId);
    List<Map<String, Object>> findSessionsByInstructorId(@Param("instructorId") String instructorId);
}