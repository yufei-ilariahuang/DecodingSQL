package com.example.demo.service;

import com.example.demo.dto.InstructorDto;
import com.example.demo.dto.StudentDto;
import com.example.demo.mapper.InstructorMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Instructor;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for handling authentication using MyBatis mappers
 */
@Service
public class AuthService {

    private final StudentMapper studentMapper;
    private final InstructorMapper instructorMapper;

    @Autowired
    public AuthService(StudentMapper studentMapper, InstructorMapper instructorMapper) {
        this.studentMapper = studentMapper;
        this.instructorMapper = instructorMapper;
    }

    /**
     * Authenticate a student by email and password
     *
     * @param email student email
     * @param password student password
     * @return StudentDto if authentication successful, null otherwise
     */
    public StudentDto authenticateStudent(String email, String password) {
        Student student = studentMapper.findByEmail(email);

        if (student != null && student.getPassword().equals(password)) {
            return convertToStudentDto(student);
        }

        return null;
    }

    /**
     * Authenticate an instructor by email and password
     *
     * @param email instructor email
     * @param password instructor password
     * @return InstructorDto if authentication successful, null otherwise
     */
    public InstructorDto authenticateInstructor(String email, String password) {
        Instructor instructor = instructorMapper.findByEmail(email);

        if (instructor != null && instructor.getPassword().equals(password)) {
            return convertToInstructorDto(instructor);
        }

        return null;
    }

    /**
     * Convert Student entity to StudentDto
     */
    private StudentDto convertToStudentDto(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .username(student.getUsername())
                .email(student.getEmail())
                .introContent(student.getIntroContent())
                .build();
    }

    /**
     * Convert Instructor entity to InstructorDto
     */
    private InstructorDto convertToInstructorDto(Instructor instructor) {
        return InstructorDto.builder()
                .id(instructor.getId())
                .username(instructor.getUsername())
                .email(instructor.getEmail())
                .introContent(instructor.getIntroContent())
                .build();
    }
}