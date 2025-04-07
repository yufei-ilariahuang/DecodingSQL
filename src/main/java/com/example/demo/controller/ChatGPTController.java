package com.example.demo.controller;

import com.example.demo.service.ChatGPTService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {


    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping
    public String chat(@RequestBody  String userPrompt) {
        return chatGPTService.getChatGPTResponse(userPrompt);
    }
}
