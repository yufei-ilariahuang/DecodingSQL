package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;
import com.example.demo.service.SQLNaturalLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final SQLNaturalLanguageService sqlNLPService;

    @Autowired
    public MessageController(MessageService messageService, SQLNaturalLanguageService sqlNLPService) {
        this.messageService = messageService;
        this.sqlNLPService = sqlNLPService;
    }

    /**
     * Get a message by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable String id) {
        Message message = messageService.getMessage(id);
        if (message == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(message);
    }

    /**
     * Create a new message
     */
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    /**
     * Update a message
     */
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable String id, @RequestBody Message message) {
        try {
            Message updatedMessage = messageService.updateMessage(id, message);
            return ResponseEntity.ok(updatedMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get messages with dynamic filters
     */
    @GetMapping("/search")
    public ResponseEntity<List<Message>> searchMessages(
            @RequestParam(required = false) String senderId,
            @RequestParam(required = false) String receiverId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate,
            @RequestParam(required = false) String content,
            @RequestParam(required = false, defaultValue = "created_time") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset) {

        List<Message> messages = messageService.findMessagesWithFilters(
                senderId, receiverId, startDate, endDate, content,
                sortField, sortDirection, limit, offset);

        return ResponseEntity.ok(messages);
    }

    /**
     * Get conversation between users with dynamic parameters
     */
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam String user1Id,
            @RequestParam(required = false) String user2Id,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer daysBack,
            @RequestParam(required = false, defaultValue = "ASC") String orderDirection,
            @RequestParam(required = false) Integer limit) {

        List<Message> messages = messageService.findConversationDynamic(
                user1Id, user2Id, keyword, daysBack, orderDirection, limit);

        return ResponseEntity.ok(messages);
    }

    /**
     * Get recent conversations for a user
     */
    @GetMapping("/recent-conversations")
    public ResponseEntity<List<Map<String, Object>>> getRecentConversations(
            @RequestParam String userId,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {

        List<Map<String, Object>> conversations = messageService.findRecentConversations(userId, limit);
        return ResponseEntity.ok(conversations);
    }

    /**
     * Advanced search with complex criteria
     */
    @PostMapping("/advanced-search")
    public ResponseEntity<List<Message>> advancedSearch(@RequestBody Map<String, Object> searchCriteria) {
        String searchTerm = (String) searchCriteria.get("searchTerm");

        @SuppressWarnings("unchecked")
        List<String> userIds = (List<String>) searchCriteria.get("userIds");

        @SuppressWarnings("unchecked")
        List<String> excludeUserIds = (List<String>) searchCriteria.get("excludeUserIds");

        Date startDate = null;
        Date endDate = null;

        @SuppressWarnings("unchecked")
        Map<String, String> dateRange = (Map<String, String>) searchCriteria.get("dateRange");
        if (dateRange != null) {
            try {
                if (dateRange.get("startDate") != null) {
                    startDate = new Date(Long.parseLong(dateRange.get("startDate")));
                }
                if (dateRange.get("endDate") != null) {
                    endDate = new Date(Long.parseLong(dateRange.get("endDate")));
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        Integer limit = (Integer) searchCriteria.get("limit");
        Integer offset = (Integer) searchCriteria.get("offset");

        List<Message> messages = messageService.searchMessages(
                searchTerm, userIds, excludeUserIds, startDate, endDate, limit, offset);

        return ResponseEntity.ok(messages);
    }

}