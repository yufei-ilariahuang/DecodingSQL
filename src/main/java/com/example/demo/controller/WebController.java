package com.example.demo.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for web pages in the DecodingSQL application
 */
@Controller
@RequiredArgsConstructor

public class WebController {
    @GetMapping("/auth")
    public String showLoginRegisterPage() {
        return "login-register";
    }
}
