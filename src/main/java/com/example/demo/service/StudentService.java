package com.example.demo.service;

import com.example.demo.dto.StudentDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public StudentDto getStudent(String id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        return convertToDto(student);
    }

    public List<StudentDto> getAllStudents() {
        return studentMapper.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudentByUsername(String username) {
        Student student = (Student) studentMapper.findByUsername(username);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with username: " + username);
        }
        return convertToDto(student);
    }

    public StudentDto getStudentByEmail(String email) {
        Student student = studentMapper.findByEmail(email);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with email: " + email);
        }
        return convertToDto(student);
    }

    // Helper method to convert Student entity to StudentDto
    private StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .id(student.getStudentId())
                .username(student.getUsername())
                .email(student.getEmail())
                .introContent(student.getIntroContent())
                // Don't include password in DTO for security
                .build();
    }

    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        // Check if username or email already exists
        if (studentMapper.existsByUsername(studentDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (studentMapper.existsByEmail(studentDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Create new student
        Student student = new Student();

        // Generate a student ID if none provided
        if (studentDto.getId() == null || studentDto.getId().isEmpty()) {
            student.setStudentId("student_" + UUID.randomUUID().toString().substring(0, 6));
        } else {
            student.setStudentId(studentDto.getId());
        }

        student.setUsername(studentDto.getUsername());
        student.setEmail(studentDto.getEmail());
        student.setPassword(studentDto.getPassword());
        student.setIntroContent(studentDto.getIntroContent());

        studentMapper.insert(student);
        return convertToDto(student);
    }

    @Transactional
    public StudentDto updateStudent(String id, StudentDto studentDto) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }

        // Check if username is being changed and if it's already taken
        if (!student.getUsername().equals(studentDto.getUsername())
                && studentMapper.existsByUsername(studentDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Check if email is being changed and if it's already taken
        if (!student.getEmail().equals(studentDto.getEmail())
                && studentMapper.existsByEmail(studentDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Update fields
        student.setUsername(studentDto.getUsername());
        student.setEmail(studentDto.getEmail());
        if (studentDto.getPassword() != null && !studentDto.getPassword().isEmpty()) {
            student.setPassword(studentDto.getPassword());
        }
        student.setIntroContent(studentDto.getIntroContent());

        studentMapper.update(student);
        return convertToDto(student);
    }

    @Transactional
    public void deleteStudent(String id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentMapper.deleteById(id);
    }

    /**
     * Authenticates a student by email and password
     *
     * @param email The student's email
     * @param password The student's password (plain text for now)
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateStudent(String email, String password) {
        try {
            Student student = studentMapper.findByEmail(email);
            if (student == null) {
                return false;
            }
            // In a real application, you should use a password encoder
            return student.getPassword().equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Logs in a student and returns their data
     *
     * @param email The student's email
     * @param password The student's password
     * @return StudentDto if login successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public StudentDto loginStudent(String email, String password) {
        if (authenticateStudent(email, password)) {
            return getStudentByEmail(email);
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}