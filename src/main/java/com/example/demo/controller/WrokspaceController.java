//package com.example.demo.controller;
//
//import com.example.demo.dto.QueryExecutionRequest;
//import com.example.demo.exception.SqlProcessingException;
//import com.example.demo.service.DynamicSqlService;
//import com.example.demo.service.ProblemService;
//import com.example.demo.service.SubmissionService;
//import jakarta.servlet.http.HttpSession;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//@Slf4j
//public class WorkspaceController {
//
//    private final DynamicSqlService dynamicSqlService;
//    private final ProblemService problemService;
//    private final SubmissionService submissionService;
//
//    @Autowired
//    public WorkspaceController(
//            DynamicSqlService dynamicSqlService,
//            ProblemService problemService,
//            SubmissionService submissionService) {
//        this.dynamicSqlService = dynamicSqlService;
//        this.problemService = problemService;
//        this.submissionService = submissionService;
//    }
//
//    /**
//     * Displays the workspace page with SQL editor
//     */
//    @GetMapping("/workspace/{problemId}")
//    public String showWorkspace(@PathVariable String problemId, Model model, HttpSession session) {
//        // Get user information from session
//        String studentId = (String) session.getAttribute("studentId");
//
//        if (studentId == null) {
//            return "redirect:/login";
//        }
//
//        // Load problem details
//        Map<String, Object> problem = problemService.getProblemById(problemId);
//
//        if (problem == null) {
//            return "redirect:/problems";
//        }
//
//        // Get previous submissions if any
//        List<Map<String, Object>> submissions = submissionService.getSubmissionsForProblem(studentId, problemId);
//
//        model.addAttribute("problem", problem);
//        model.addAttribute("submissions", submissions);
//        model.addAttribute("studentId", studentId);
//
//        return "workspace/editor";
//    }
//
//    /**
//     * Executes a SQL query and returns the results
//     */
//    @PostMapping("/api/workspace/execute")
//    @ResponseBody
//    public ResponseEntity<?> executeQuery(@RequestBody QueryExecutionRequest request, HttpSession session) {
//        // Get user information from session
//        String studentId = (String) session.getAttribute("studentId");
//
//        if (studentId == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Not authenticated"));
//        }
//
//        try {
//            // Validate and sanitize the query
//            String query = request.getQuery();
//
//            // Execute the query
//            List<Map<String, Object>> results = dynamicSqlService.executeDynamicQuery(query);
//
//            // Build response
//            Map<String, Object> response = new HashMap<>();
//            response.put("results", results);
//
//            return ResponseEntity.ok(response);
//        } catch (SqlProcessingException e) {
//            log.error("Error executing query: {}", e.getMessage());
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            log.error("Unexpected error executing query", e);
//            return ResponseEntity.badRequest().body(Map.of("error", "An unexpected error occurred"));
//        }
//    }
//
//    /**
//     * Validates a SQL solution against the expected results
//     */
//    @PostMapping("/api/submissions/submit")
//    @ResponseBody
//    public ResponseEntity<?> submitSolution(@RequestBody QueryExecutionRequest request, HttpSession session) {
//        // Get user information from session
//        String studentId = (String) session.getAttribute("studentId");
//
//        if (studentId == null) {
//            return ResponseEntity.badRequest().body(Map.of("error", "Not authenticated"));
//        }
//
//        try {
//            // Submit the solution and get feedback
//            Map<String, Object> result = submissionService.submitSolution(
//                    studentId,
//                    request.getProblemId(),
//                    request.getQuery()
//            );
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Error submitting solution", e);
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        }
//    }
//}