package com.example.demo.controller;

import com.example.demo.service.SQLNaturalLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sql")
public class SQLNaturalLanguageController {

    private final SQLNaturalLanguageService sqlNLPService;

    @Autowired
    public SQLNaturalLanguageController(SQLNaturalLanguageService sqlNLPService) {
        this.sqlNLPService = sqlNLPService;
    }

    /**
     * Process a natural language query and return the results
     */
    @PostMapping("/query")
    public ResponseEntity<?> processNaturalLanguageQuery(@RequestBody Map<String, String> request) {

        try {
            // Extract the natural language query from the request
            String query = request.get("query");
            String userId = request.get("userId");
            if (query == null || query.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Query cannot be empty"));
            }
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "User ID cannot be empty"));
            }

            // Process the natural language query and get results
            List<Map<String, Object>> results = sqlNLPService.processNaturalLanguageQuery(query, userId);

            // Build response with results
            Map<String, Object> response = new HashMap<>();
            response.put("query", query);
            response.put("results", results);
            response.put("count", results.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}