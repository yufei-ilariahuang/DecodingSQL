package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthCheckController {

    @GetMapping("/check")
    public ResponseEntity<?> checkAuthStatus(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", isAuthenticated != null && isAuthenticated);

        if (isAuthenticated != null && isAuthenticated && userRole != null) {
            response.put("userRole", userRole);

            if ("student".equals(userRole)) {
                response.put("userId", session.getAttribute("studentId"));
                response.put("username", session.getAttribute("username"));
            } else if ("instructor".equals(userRole)) {
                response.put("userId", session.getAttribute("instructorId"));
                response.put("username", session.getAttribute("username"));
            }
        }

        return ResponseEntity.ok(response);
    }
}