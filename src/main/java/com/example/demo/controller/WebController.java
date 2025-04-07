package com.example.demo.controller;

import com.example.demo.mapper.CourseMetadataMapper;
import com.example.demo.mapper.InstructorMapper;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.mapper.SessionSubmissionMapper;
import com.example.demo.mapper.SqlProblemMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Controller for web pages in the DecodingSQL application
 */
@Controller
@RequiredArgsConstructor
public class WebController {

    private final StudentMapper studentMapper;
    private final InstructorMapper instructorMapper;
    private final SessionSubmissionMapper sessionSubmissionMapper;
    private final SqlProblemMapper sqlProblemMapper;
    private final MessageMapper messageMapper;
    private final CourseMetadataMapper courseMetadataMapper; // Add this new dependency

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/auth")
    public String showLoginRegisterPage(HttpSession session) {
        // Check if user is already authenticated
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        if (isAuthenticated != null && isAuthenticated) {
            return "redirect:/dashboard";
        }

        return "login-register";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if user is authenticated
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        if (isAuthenticated == null || !isAuthenticated) {
            return "redirect:/auth";
        }

        String userRole = (String) session.getAttribute("userRole");
        System.out.println("DEBUG - Session ID: " + session.getId());
        System.out.println("DEBUG - isAuthenticated: " + isAuthenticated);
        System.out.println("DEBUG - userRole: " + userRole);
        if ("student".equals(userRole)) {
            return studentDashboard(session, model);
        } else if ("instructor".equals(userRole)) {
            return instructorDashboard(session, model);
        }

        return "redirect:/auth";
    }

    @GetMapping("/student/dashboard")
    public String studentDashboardRedirect(HttpSession session, Model model) {
        // Check if user is authenticated as student
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        if (isAuthenticated != null && isAuthenticated && "student".equals(userRole)) {
            return studentDashboard(session, model);
        }

        return "redirect:/auth";
    }

    @GetMapping("/instructor/dashboard")
    public String instructorDashboardRedirect(HttpSession session, Model model) {
        // Check if user is authenticated as instructor
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        String userRole = (String) session.getAttribute("userRole");

        if (isAuthenticated != null && isAuthenticated && "instructor".equals(userRole)) {
            return instructorDashboard(session, model);
        }

        return "redirect:/auth";
    }
    // Helper method to get recent messages
    private List<Map<String, Object>> getRecentMessages(String studentId, int limit) {
        // Use the dynamic SQL functionality
        Map<String, Object> params = new HashMap<>();
        params.put("receiverId", studentId);
        params.put("limit", limit);

        List<Message> messageList = messageMapper.findMessagesWithFilters(params);
        List<Map<String, Object>> formattedMessages = new ArrayList<>();

        for (Message message : messageList) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("id", message.getId());
            messageMap.put("senderId", message.getSenderId());
            messageMap.put("senderName", getSenderName(message.getSenderId()));
            messageMap.put("receiverId", message.getReceiverId());
            messageMap.put("content", message.getContent());
            messageMap.put("createdTime", message.getCreatedTime());
            formattedMessages.add(messageMap);
        }

        return formattedMessages;
    }

    // Helper method to get sender name
    private String getSenderName(String senderId) {
        if (senderId.startsWith("student_")) {
            // Get student name
            Student student = studentMapper.findById(senderId);
            return student != null ? student.getUsername() : "Unknown Student";
        } else if (senderId.startsWith("instructor_")) {
            // Get instructor name
            Instructor instructor = instructorMapper.findById(senderId);
            return instructor != null ? instructor.getUsername() : "Unknown Instructor";
        }
        return "Unknown";
    }
    private String studentDashboard(HttpSession session, Model model) {
        String studentId = (String) session.getAttribute("studentId");
        String username = (String) session.getAttribute("username");
        // Get student information
        Student student = studentMapper.findById(studentId);

        // Get student submissions
        List<SessionSubmission> submissions = sessionSubmissionMapper.findByStudentId(studentId);
        System.out.println("Submissions found: " + submissions.size());
        List<Map<String, Object>> recentSubmissions = new ArrayList<>();
        // Get recent messages
        List<Map<String, Object>> messages = getRecentMessages(studentId, 5);


        // Get course data using CourseMetadata
        List<Map<String, Object>> courses = courseMetadataMapper.getStudentCourses(studentId);

        // Format submissions for the view
        for (SessionSubmission submission : submissions) {
            try {
                Map<String, Object> submissionMap = new HashMap<>();
                SqlProblem problem = sqlProblemMapper.findById(submission.getProblemId());
                submissionMap.put("problemTitle", problem != null ? problem.getTitle() : "Unknown Problem");
                submissionMap.put("status", submission.getGrade() >= 80 ? "Correct" : "Incorrect");
                submissionMap.put("score", submission.getGrade());
                recentSubmissions.add(submissionMap);

                // Limit to 5 recent submissions
                if (recentSubmissions.size() >= 5) {
                    break;
                }
            } catch (Exception e) {
                System.err.println("Error processing submission " + submission.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }

        }

        // Prepare stats data
        Map<String, Object> stats = new HashMap<>();
        stats.put("currentGPA", calculateGPA(studentId)); // Calculate actual GPA
        stats.put("problemsSolved", submissions.size());
        stats.put("totalProblems", sqlProblemMapper.findAll().size());
        stats.put("averageGrade", calculateAverageGrade(submissions));
        stats.put("pendingAssignments", calculatePendingAssignments(studentId)); // Calculate actual pending assignments
        // Add all data to the model
        model.addAttribute("username", username);
        model.addAttribute("studentId", studentId);
        model.addAttribute("student", student);
        model.addAttribute("recentSubmissions", recentSubmissions);
        model.addAttribute("messages", messages);
        model.addAttribute("courses", courses);
        model.addAttribute("stats", stats);

        return "dashboard/student";
    }

    private String instructorDashboard(HttpSession session, Model model) {
        String instructorId = (String) session.getAttribute("instructorId");
        String username = (String) session.getAttribute("username");

        // Find problems created by this instructor
        List<com.example.demo.model.SqlProblem> problems = sqlProblemMapper.findByCreator(instructorId);

        // Get student submissions for instructor's problems
        List<Map<String, Object>> recentSubmissions = new ArrayList<>();
        for (com.example.demo.model.SqlProblem problem : problems) {
            List<SessionSubmission> submissions = sessionSubmissionMapper.findByProblemId(problem.getId());
            for (SessionSubmission submission : submissions) {
                Map<String, Object> submissionMap = new HashMap<>();
                submissionMap.put("studentName", studentMapper.findById(submission.getStudentId()).getUsername());
                submissionMap.put("problemTitle", problem.getTitle());
                submissionMap.put("status", submission.getGrade() != null ? "Graded" : "Pending");
                recentSubmissions.add(submissionMap);

                // Limit to 5 recent submissions
                if (recentSubmissions.size() >= 5) {
                    break;
                }
            }
            if (recentSubmissions.size() >= 5) {
                break;
            }
        }

        // Get instructor's courses
        List<Map<String, Object>> courses = getInstructorCourses(instructorId);

        // Prepare stats data
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", calculateTotalStudents(instructorId)); // Calculate from courses
        stats.put("totalCourses", courses.size());
        stats.put("averageGrade", calculateInstructorAverageGrade(instructorId));
        stats.put("pendingGrading", calculatePendingGrading(instructorId));
        stats.put("problemsCreated", problems.size());

        model.addAttribute("username", username);
        model.addAttribute("instructorId", instructorId);
        model.addAttribute("recentSubmissions", recentSubmissions);
        model.addAttribute("stats", stats);
        model.addAttribute("courses", courses);

        return "dashboard/instructor";
    }

    // Helper methods

    private double calculateAverageGrade(List<SessionSubmission> submissions) {
        if (submissions.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;
        int count = 0;

        for (SessionSubmission submission : submissions) {
            if (submission.getGrade() != null) {
                total += submission.getGrade();
                count++;
            }
        }

        return count > 0 ? Math.round((total / count) * 10) / 10.0 : 0.0;
    }

    private String calculateGPA(String studentId) {
        // Use the submission grades to calculate GPA
        List<SessionSubmission> submissions = sessionSubmissionMapper.findByStudentId(studentId);
        if (submissions.isEmpty()) {
            return "0.0";
        }

        double totalPoints = 0;
        int totalSubmissions = 0;

        for (SessionSubmission submission : submissions) {
            if (submission.getGrade() != null) {
                double grade = submission.getGrade();

                // Convert percentage to 4.0 scale
                double gpaPoints = 0;
                if (grade >= 90) {
                    gpaPoints = 4.0; // A
                } else if (grade >= 80) {
                    gpaPoints = 3.0; // B
                } else if (grade >= 70) {
                    gpaPoints = 2.0; // C
                } else if (grade >= 60) {
                    gpaPoints = 1.0; // D
                }

                totalPoints += gpaPoints;
                totalSubmissions++;
            }
        }

        double gpa = totalSubmissions > 0 ? totalPoints / totalSubmissions : 0;
        return String.format("%.1f", gpa); // Format to 1 decimal place
    }

    private int calculatePendingAssignments(String studentId) {
        // Count problems assigned to student that haven't been submitted yet
        int pendingCount = 0;

        // Get all sessions (courses) the student is enrolled in
        List<Map<String, Object>> trainingSessionsResult = courseMetadataMapper.findSessionsByStudentId(studentId);

        for (Map<String, Object> session : trainingSessionsResult) {
            String problemIds = (String) session.get("problem_ids");
            String sessionId = (String) session.get("session_id");

            if (problemIds != null && !problemIds.isEmpty()) {
                String[] problems = problemIds.split(",");

                for (String problemId : problems) {
                    // Check if student has submitted this problem
                    List<SessionSubmission> submissions = sessionSubmissionMapper.findByStudentIdAndProblemId(studentId, problemId.trim());
                    if (submissions.isEmpty()) {
                        pendingCount++;
                    }
                }
            }
        }

        return pendingCount;
    }

    private int calculateTotalStudents(String instructorId) {
        // Get unique count of students across all instructor's courses
        List<Map<String, Object>> coursesResult = courseMetadataMapper.findSessionsByInstructorId(instructorId);

        // Use a Set to track unique student IDs
        java.util.Set<String> uniqueStudents = new java.util.HashSet<>();

        for (Map<String, Object> course : coursesResult) {
            String studentIds = (String) course.get("student_ids");
            if (studentIds != null && !studentIds.isEmpty()) {
                String[] students = studentIds.split(",");
                for (String studentId : students) {
                    uniqueStudents.add(studentId.trim());
                }
            }
        }

        return uniqueStudents.size();
    }

    private double calculateInstructorAverageGrade(String instructorId) {
        // Calculate average grade across all problems created by instructor
        List<SqlProblem> problems = sqlProblemMapper.findByCreator(instructorId);
        List<Double> allGrades = new ArrayList<>();

        for (SqlProblem problem : problems) {
            List<SessionSubmission> submissions = sessionSubmissionMapper.findByProblemId(problem.getId());
            for (SessionSubmission submission : submissions) {
                if (submission.getGrade() != null) {
                    allGrades.add((double) submission.getGrade());
                }
            }
        }

        if (allGrades.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (Double grade : allGrades) {
            sum += grade;
        }

        return Math.round((sum / allGrades.size()) * 10) / 10.0;
    }

    private int calculatePendingGrading(String instructorId) {
        // Count submissions for instructor's problems that don't have grades
        List<SqlProblem> problems = sqlProblemMapper.findByCreator(instructorId);
        int pendingCount = 0;

        for (SqlProblem problem : problems) {
            List<SessionSubmission> submissions = sessionSubmissionMapper.findByProblemId(problem.getId());
            for (SessionSubmission submission : submissions) {
                if (submission.getGrade() == null) {
                    pendingCount++;
                }
            }
        }

        return pendingCount;
    }

    // Course data methods using stored procedures

    private List<Map<String, Object>> getStudentCourses(String studentId) {
        // Call the stored procedure through mapper
        List<Map<String, Object>> coursesResult = courseMetadataMapper.getStudentCourses(studentId);

        // Format the results as needed for the view
        List<Map<String, Object>> courses = new ArrayList<>();

        for (Map<String, Object> result : coursesResult) {
            Map<String, Object> course = new HashMap<>();
            course.put("id", result.get("id"));
            course.put("title", result.get("title"));
            course.put("instructor", result.get("instructor"));
            course.put("grade", result.get("grade"));
            course.put("credits", result.get("credits"));
            course.put("progress", result.get("progress"));
            courses.add(course);
        }

        return courses;
    }

    private List<Map<String, Object>> getInstructorCourses(String instructorId) {
        // Call the stored procedure through mapper
        List<Map<String, Object>> coursesResult = courseMetadataMapper.getInstructorCourses(instructorId);

        // Format the results as needed for the view
        List<Map<String, Object>> courses = new ArrayList<>();

        for (Map<String, Object> result : coursesResult) {
            Map<String, Object> course = new HashMap<>();
            course.put("id", result.get("id"));
            course.put("title", result.get("title"));
            course.put("students", result.get("students"));
            course.put("problems", result.get("problems"));
            course.put("averageGrade", result.get("averageGrade"));
            courses.add(course);
        }

        return courses;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}