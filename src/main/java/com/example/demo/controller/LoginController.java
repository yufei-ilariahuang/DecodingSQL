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

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final StudentService studentService;
    private final InstructorService instructorService;

    @Autowired
    public LoginController(StudentService studentService, InstructorService instructorService) {
        this.studentService = studentService;
        this.instructorService = instructorService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            // Authenticate based on user type
            if ("student".equalsIgnoreCase(request.getUserType())) {
                // Authenticate student
                StudentDto student = studentService.loginStudent(request.getEmail(), request.getPassword());

                // Set session attributes
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("userRole", "student");
                session.setAttribute("studentId", student.getId());
                session.setAttribute("username", student.getUsername());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("userId", student.getId());
                response.put("username", student.getUsername());
                response.put("userType", "student");

                return ResponseEntity.ok(response);

            } else if ("instructor".equalsIgnoreCase(request.getUserType())) {
                // Authenticate instructor
                InstructorDto instructor = instructorService.loginInstructor(request.getEmail(), request.getPassword());

                // Set session attributes
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("userRole", "instructor");
                session.setAttribute("instructorId", instructor.getId());
                session.setAttribute("username", instructor.getUsername());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("userId", instructor.getId());
                response.put("username", instructor.getUsername());
                response.put("userType", "instructor");

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid user type"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed: " + e.getMessage()));
        }
    }

    // Inner class for login request
    public static class LoginRequest {
        private String email;
        private String password;
        private String userType;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getUserType() { return userType; }
        public void setUserType(String userType) { this.userType = userType; }
    }
}