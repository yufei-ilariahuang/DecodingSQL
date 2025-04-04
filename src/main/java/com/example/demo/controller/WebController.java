package com.example.demo.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
