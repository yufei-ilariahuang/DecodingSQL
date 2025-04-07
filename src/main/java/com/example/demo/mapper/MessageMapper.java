package com.example.demo.mapper;

import com.example.demo.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface MessageMapper {
    // Basic CRUD methods
    Message findById(@Param("messageId") String messageId);
    List<Message> findAll();
    int insert(Message message);
    int update(Message message);
    int deleteById(@Param("messageId") String messageId);

    // Existing methods
    List<Message> findBySenderId(@Param("senderId") String senderId);
    List<Message> findByReceiverId(@Param("receiverId") String receiverId);
    List<Message> findConversation(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);
    List<Message> findByCreatedTimeAfter(@Param("time") Date time);

    // Dynamic SQL methods
    List<Message> findMessagesWithFilters(Map<String, Object> params);

    List<Message> findConversationDynamic(Map<String, Object> params);

    List<Map<String, Object>> findRecentConversations(@Param("userId") String userId,
                                                      @Param("limit") Integer limit);

    List<Message> searchMessages(Map<String, Object> searchCriteria);

    /**
     * Execute a natural language query with additional safety checks
     * @param dynamicSql The SQL query to execute

     * @return List of results
     */
    List<Map<String, Object>> executeNaturalLanguageQuery(
            @Param("dynamicSql") String dynamicSql
    );
}