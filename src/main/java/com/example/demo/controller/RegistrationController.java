package com.example.demo.controller;

import com.example.demo.dto.StudentDto;
import com.example.demo.dto.InstructorDto;
import com.example.demo.service.StudentService;
import com.example.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final StudentService studentService;
    private final InstructorService instructorService;

    @Autowired
    public RegistrationController(StudentService studentService, InstructorService instructorService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            // Validate passwords match
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Passwords do not match"));
            }

            // Register based on user type
            if ("student".equalsIgnoreCase(request.getUserType())) {
                StudentDto studentDto = StudentDto.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build();

                StudentDto createdStudent = studentService.createStudent(studentDto);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Student registered successfully");
                response.put("userId", createdStudent.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else if ("instructor".equalsIgnoreCase(request.getUserType())) {
                InstructorDto instructorDto = InstructorDto.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build();

                InstructorDto createdInstructor = instructorService.createInstructor(instructorDto);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Instructor registered successfully");
                response.put("userId", createdInstructor.getId());

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid user type"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Registration failed: " + e.getMessage()));
        }
    }

    public static class RegistrationRequest {
        private String username;
        private String email;
        private String password;
        private String confirmPassword;
        private String userType;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }
}