package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

//    @GetMapping
//    public String dashboard(HttpSession session, Model model) {
//        String userRole = (String) session.getAttribute("userRole");
//
//        if (userRole == null) {
//            return "redirect:/login-register";
//        }
//
//        if ("student".equals(userRole)) {
//            return studentDashboard(session, model);
//        } else if ("instructor".equals(userRole)) {
//            return instructorDashboard(session, model);
//        }
//
//        return "redirect:/login-register";
//    }

//    private String studentDashboard(HttpSession session, Model model) {
//        String studentId = (String) session.getAttribute("studentId");
//        String username = (String) session.getAttribute("username");
//
//        // Sample data - replace with actual database queries
//        List<Map<String, Object>> courses = getSampleStudentCourses();
//        Map<String, Object> stats = getSampleStudentStats();
//        List<Map<String, Object>> recentSubmissions = getSampleRecentSubmissions();
//
//        model.addAttribute("username", username);
//        model.addAttribute("studentId", studentId);
//        model.addAttribute("courses", courses);
//        model.addAttribute("stats", stats);
//        model.addAttribute("recentSubmissions", recentSubmissions);
//
//        return "dashboard/student";
//    }
//
//    private String instructorDashboard(HttpSession session, Model model) {
//        String instructorId = (String) session.getAttribute("instructorId");
//        String username = (String) session.getAttribute("username");
//
//        // Sample data - replace with actual database queries
//        List<Map<String, Object>> courses = getSampleInstructorCourses();
//        List<Map<String, Object>> recentSubmissions = getSampleInstructorSubmissions();
//        Map<String, Object> stats = getSampleInstructorStats();
//
//        model.addAttribute("username", username);
//        model.addAttribute("instructorId", instructorId);
//        model.addAttribute("courses", courses);
//        model.addAttribute("stats", stats);
//        model.addAttribute("recentSubmissions", recentSubmissions);
//
//        return "dashboard/instructor";
//    }

//    }
}