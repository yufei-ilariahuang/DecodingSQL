package com.example.demo.service;

import com.example.demo.dto.StudentDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.google.api.gax.rpc.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentDto getStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return StudentDto.fromEntity(student);
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentDto::fromEntity)
                .collect(Collectors.toList());
    }
    public StudentDto getStudentByUsername(String username) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with username: " + username));
        return StudentDto.fromEntity(student);
    }
    // Helper method to convert Student entity to StudentDto
    private StudentDto convertToDto(Student student) {
        return StudentDto.builder()
                .id(student.getStudentId())
                .username(student.getUsername())
                .email(student.getEmail())
                .introContent(student.getIntroContent())
                .build();
    }
    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        // Check if username or email already exists
        if (studentRepository.existsByUsername(studentDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Create new student
        Student student = new Student();
        student.setStudentId(studentDto.getId());
        student.setUsername(studentDto.getUsername());
        student.setEmail(studentDto.getEmail());
        student.setPassword(studentDto.getPassword());
        student.setIntroContent(studentDto.getIntroContent());

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Transactional
    public StudentDto updateStudent(String id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Update fields
        student.setUsername(studentDto.getUsername());
        student.setEmail(studentDto.getEmail());
        if (studentDto.getPassword() != null && !studentDto.getPassword().isEmpty()) {
            student.setPassword(studentDto.getPassword());
        }
        student.setIntroContent(studentDto.getIntroContent());

        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    @Transactional
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }


    // Other business methods
}
