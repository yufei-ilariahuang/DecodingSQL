package com.example.demo.service;

import com.example.demo.mapper.MessageMapper;
import com.example.demo.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MessageService {

    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * Get a message by ID
     */
    public Message getMessage(String id) {
        return messageMapper.findById(id);
    }

    /**
     * Get all messages
     */
    public List<Message> getAllMessages() {
        return messageMapper.findAll();
    }

    /**
     * Get messages sent by a specific user
     */
    public List<Message> getMessagesBySender(String senderId) {
        return messageMapper.findBySenderId(senderId);
    }

    /**
     * Get messages received by a specific user
     */
    public List<Message> getMessagesByReceiver(String receiverId) {
        return messageMapper.findByReceiverId(receiverId);
    }

    /**
     * Get conversation between two users
     */
    public List<Message> getConversation(String user1Id, String user2Id) {
        return messageMapper.findConversation(user1Id, user2Id);
    }

    /**
     * Create a new message
     */
    @Transactional
    public Message createMessage(Message message) {
        // Generate ID if not provided
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId("msg_" + UUID.randomUUID().toString().substring(0, 8));
        }

        // Set created time if not provided
        if (message.getCreatedTime() == null) {
            message.setCreatedTime(new Date());
        }

        messageMapper.insert(message);
        return message;
    }

    /**
     * Update an existing message
     */
    @Transactional
    public Message updateMessage(String id, Message message) {
        Message existingMessage = messageMapper.findById(id);
        if (existingMessage == null) {
            throw new IllegalArgumentException("Message not found with id: " + id);
        }

        message.setId(id);
        messageMapper.update(message);
        return message;
    }

    /**
     * Delete a message
     */
    @Transactional
    public void deleteMessage(String id) {
        messageMapper.deleteById(id);
    }

    /**
     * Find messages with dynamic filters
     */
    public List<Message> findMessagesWithFilters(
            String senderId,
            String receiverId,
            Date startDate,
            Date endDate,
            String content,
            String sortField,
            String sortDirection,
            Integer limit,
            Integer offset) {

        Map<String, Object> params = new HashMap<>();
        params.put("senderId", senderId);
        params.put("receiverId", receiverId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("content", content);
        params.put("sortField", sortField);
        params.put("sortDirection", sortDirection);
        params.put("limit", limit);
        params.put("offset", offset);

        return messageMapper.findMessagesWithFilters(params);
    }

    /**
     * Find conversation with dynamic parameters
     */
    public List<Message> findConversationDynamic(
            String user1Id,
            String user2Id,
            String keyword,
            Integer daysBack,
            String orderDirection,
            Integer limit) {

        Map<String, Object> params = new HashMap<>();
        params.put("user1Id", user1Id);
        params.put("user2Id", user2Id);
        params.put("keyword", keyword);
        params.put("daysBack", daysBack);
        params.put("orderDirection", orderDirection);
        params.put("limit", limit);

        return messageMapper.findConversationDynamic(params);
    }

    /**
     * Find recent conversations for a user
     */
    public List<Map<String, Object>> findRecentConversations(String userId, Integer limit) {
        return messageMapper.findRecentConversations(userId, limit);
    }

    /**
     * Search messages with complex criteria
     */
    public List<Message> searchMessages(
            String searchTerm,
            List<String> userIds,
            List<String> excludeUserIds,
            Date startDate,
            Date endDate,
            Integer limit,
            Integer offset) {

        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);
        params.put("userIds", userIds);
        params.put("excludeUserIds", excludeUserIds);

        Map<String, Date> dateRange = new HashMap<>();
        dateRange.put("startDate", startDate);
        dateRange.put("endDate", endDate);
        params.put("dateRange", dateRange);

        params.put("limit", limit);
        params.put("offset", offset);

        return messageMapper.searchMessages(params);
    }
}