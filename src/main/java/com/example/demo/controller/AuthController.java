package com.example.demo.controller;

import com.example.demo.dto.StudentDto;
import com.example.demo.service.StudentService;
import com.example.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            // Determine user type
            String userType = loginRequest.getUserType();
            if (userType == null || userType.isEmpty()) {
                userType = "student"; // Default to student if not specified
            }

            if ("student".equalsIgnoreCase(userType)) {
                // Authenticate student
                boolean isAuthenticated = studentService.authenticateStudent(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                );

                if (isAuthenticated) {
                    StudentDto student = studentService.getStudentByEmail(loginRequest.getEmail());

                    // Store user info in session
                    session.setAttribute("userId", student.getId());
                    session.setAttribute("userEmail", student.getEmail());
                    session.setAttribute("userType", "student");
                    session.setAttribute("isAuthenticated", true);

                    return ResponseEntity.ok(Map.of(
                            "user", student,
                            "userType", "student"
                    ));
                }
            } else if ("instructor".equalsIgnoreCase(userType)) {
                // Authenticate instructor
                boolean isAuthenticated = instructorService.authenticateInstructor(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                );

                if (isAuthenticated) {
                    var instructor = instructorService.getInstructorByEmail(loginRequest.getEmail());

                    // Store user info in session
                    session.setAttribute("userId", instructor.getId());
                    session.setAttribute("userEmail", instructor.getEmail());
                    session.setAttribute("userType", "instructor");
                    session.setAttribute("isAuthenticated", true);

                    return ResponseEntity.ok(Map.of(
                            "user", instructor,
                            "userType", "instructor"
                    ));
                }
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Invalid user type"));
            }

            // If authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Login failed: " + e.getMessage()));
        }
    }

    // Updated LoginRequest class
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