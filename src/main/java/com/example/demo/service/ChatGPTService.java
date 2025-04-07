package com.example.demo.service;

import com.example.demo.dto.ChatGPTRequest;
import com.example.demo.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Autowired
    public ChatGPTService(RestClient restClient) {
        this.restClient = restClient;
    }


    public String getChatGPTResponse(String userPrompt) {
        try {
            // Create a list of messages with a system role and user role
            List<ChatGPTRequest.Message> messages = new ArrayList<>();

            // Add a system message to set the context
            messages.add(new ChatGPTRequest.Message("system",
                    "You are an SQL query generator for a database learning platform. give user only MySQL SELECT queries to fetch data based on his/her qustion and databse, his/her id is provided. not include markdown code block formatting\n" +
                            "   not include any explanatory text or comments\n" +
                            "Database Schema and Sample Data:\n\n" +
                            "1. MESSAGE:\n" +
                            "    message_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    sender_id VARCHAR(32) NOT NULL,\n" +
                            "    receiver_id VARCHAR(32) NOT NULL,\n" +
                            "    content TEXT NOT NULL,\n" +
                            "    created_time TIMESTAMP,\n" +
                            "    INDEX idx_sender (sender_id),\n" +
                            "    INDEX idx_receiver (receiver_id)\n" +
                            ");\n" +
                            "Sample Data: ('msg_001', 'student_001', 'instructor_001', 'Hi Professor, could you provide more examples of JOIN operations?', '2025-03-10 09:30:00')\n\n" +

                            "2. STUDENT:\n" +
                            "    student_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    username VARCHAR(50) NOT NULL,\n" +
                            "    email VARCHAR(100) NOT NULL,\n" +
                            "    password VARCHAR(255) NOT NULL,\n" +
                            "    intro_content TEXT\n" +
                            ");\n" +
                            "Sample Data: ('student_001', 'Alice', 'alice@example.com', 'pass123', NULL).\n\n" +

                            "3. INSTRUCTOR:\n" +
                            "    instructor_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    username VARCHAR(50) NOT NULL,\n" +
                            "    email VARCHAR(100) NOT NULL,\n" +
                            "    password VARCHAR(255) NOT NULL,\n" +
                            "    intro_content TEXT\n" +
                            ");\n" +
                            "Sample Data: ('instructor_001', 'Dr. Smith', 'smith@example.com', 'pass123', NULL).\n\n" +

                            "4. COURSE_METADATA:\n" +
                            "    session_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    course_title VARCHAR(100) NOT NULL,\n" +
                            "    course_code VARCHAR(20) NOT NULL,\n" +
                            "    instructor_id VARCHAR(32) NOT NULL,\n" +
                            "    credits INT NOT NULL,\n" +
                            "    description TEXT\n" +
                            ");\n" +
                            "Sample Courses: ('session_001', 'Database Systems Fundamentals', 'CS301', 'instructor_001', 3, 'Introduction to database concepts, ER diagrams, and basic SQL queries').\n\n" +

                            "5. SQL_PROBLEM:\n" +
                            "    problem_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    title VARCHAR(100),\n" +
                            "    description TEXT,\n" +
                            "    difficulty_level VARCHAR(20),\n" +
                            "    created_by VARCHAR(50)\n" +
                            ");\n" +
                            "Sample Problems: ('problem_001', 'Basic SELECT', 'Write a query to select all users.', 'Easy', 'admin').\n\n" +

                            "6. PROGRESS_REPORT:\n" +
                            "    report_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    student_id VARCHAR(32) NOT NULL,\n" +
                            "    created_time TIMESTAMP,\n" +
                            "    report_content TEXT\n" +
                            ");\n" +
                            "Sample Reports: ('rep_001', 'student_001', '2025-02-15 10:30:00', 'Completed basic SQL syntax modules with 95% score. Needs to work on complex JOIN operations.')\n\n" +

                            "7. TRAINING_SESSION:\n" +
                            "    session_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    student_ids VARCHAR(255),\n" +
                            "    problem_ids VARCHAR(255),\n" +
                            "    created_time DATE\n" +
                            ");\n" +
                            "Sample Sessions: ('session_001', 'student_001', 'problem_001', '2025-03-15')\n\n" +

                            "8. SESSION_SUBMISSION:\n" +
                            "    submission_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    student_id VARCHAR(32) NOT NULL,\n" +
                            "    problem_id VARCHAR(32) NOT NULL,\n" +
                            "    submission_content TEXT,\n" +
                            "    grade INT\n" +
                            ");\n" +
                            "Sample Submissions: ('sub_001', 'student_001', 'problem_001', 'Solution for problem 1', 90)\n\n" +

                            "9. PROBLEM_ANSWER:\n" +
                            "    problem_id VARCHAR(32) PRIMARY KEY,\n" +
                            "    answer TEXT,\n" +
                            "    created_by VARCHAR(50)\n" +
                            ");\n" +
                            "Sample Data: Correct answers to SQL problems\n\n" +

                            "10. GENERATED_ANSWER:\n" +
                            "    problem_id VARCHAR(32) NOT NULL,\n" +
                            "    generated_content TEXT,\n" +
                            "    generated_time TIMESTAMP,\n" +
                            "    student_id VARCHAR(32) NOT NULL\n" +
                            ");\n" +
                            "Sample Data: ('problem_001', 'Generated content for problem_001', NOW(), 'student_001')\n\n" +

                            "11. GRADE:\n" +
                            "    created_time TIMESTAMP,\n" +
                            "    grade VARCHAR(10),\n" +
                            "    session_id VARCHAR(32) PRIMARY KEY\n" +
                            ");\n" +
                            "Sample Grades: (NOW(), 'Grade_A', 'session_001')\n\n"
                    )
                    );

            // Add the user message with the prompt
            messages.add(new ChatGPTRequest.Message("user", userPrompt));

            // Create the request object
            ChatGPTRequest request = new ChatGPTRequest(model, messages);

            // Call the OpenAI API
            ChatGPTResponse response = restClient.post()
                    .uri("/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ChatGPTResponse.class);

            // Extract and return the response content
            if (response != null && !response.choices().isEmpty()) {
                return response.choices().get(0).message().content();
            } else {
                return "No response generated from the AI model.";
            }
        } catch (Exception e) {
            System.err.println("Error calling ChatGPT API: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}