package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/set-session")
    public ResponseEntity<Map<String, Object>> setSession(HttpSession session) {
        session.setAttribute("isAuthenticated", true);
        session.setAttribute("userRole", "student");
        session.setAttribute("studentId", "test-student-id");
        session.setAttribute("username", "testuser");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Session set successfully");
        response.put("sessionId", session.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-session")
    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        Map<String, Object> response = new HashMap<>();
        response.put("sessionId", session.getId());
        response.put("isAuthenticated", isAuthenticated);
        response.put("userRole", userRole);

        return ResponseEntity.ok(response);
    }
}
