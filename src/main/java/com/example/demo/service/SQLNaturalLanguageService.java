package com.example.demo.service;

import com.example.demo.mapper.MessageMapper;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SQLNaturalLanguageService {
    private static final Logger logger = LoggerFactory.getLogger(SQLNaturalLanguageService.class);

    private final ChatGPTService chatGPTService;
    private final MessageMapper messageMapper;

    // List of allowed tables that can be queried
    private final List<String> allowedTables = List.of(
            "MESSAGE", "STUDENT", "INSTRUCTOR", "COURSE_METADATA",
            "SQL_PROBLEM", "PROGRESS_REPORT", "TRAINING_SESSION",
            "SESSION_SUBMISSION", "PROBLEM_ANSWER", "GENERATED_ANSWER", "GRADE"
    );

    @Autowired
    public SQLNaturalLanguageService(ChatGPTService chatGPTService, MessageMapper messageMapper) {
        this.chatGPTService = chatGPTService;
        this.messageMapper = messageMapper;
    }

    public List<Map<String, Object>> processNaturalLanguageQuery(String naturalLanguageQuery, String userId) {
        try {
            String userPrompt = "Context: This query is from ID " + userId + "\n" +
                    "Generate a precise SQL query that answers this question: \"" +
                    naturalLanguageQuery + "\"";

            // Generate SQL from natural language using ChatGPT
            String sqlQuery = generateSQLFromNaturalLanguage(userPrompt);

            // Log the generated SQL for debugging
            logger.info("Generated SQL Query: {}", sqlQuery);

            // Validate SQL query before execution
            validateSQLQuery(sqlQuery);

            // Execute the generated SQL
            return messageMapper.executeNaturalLanguageQuery(sqlQuery);
        } catch (Exception e) {
            logger.error("Error processing natural language query", e);
            throw new RuntimeException("Failed to process query: " + e.getMessage(), e);
        }
    }

    private String generateSQLFromNaturalLanguage(String userPrompt) {
        // Generate SQL using the ChatGPT service
        String response = chatGPTService.getChatGPTResponse(userPrompt);

        // Extract just the SQL query from the response
        return extractSQLFromResponse(response);
    }

    private void validateSQLQuery(String sqlQuery) {
        if (sqlQuery == null || sqlQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL query cannot be empty");
        }

        try {
            // Parse the SQL statement
            Statement statement = CCJSqlParserUtil.parse(sqlQuery);

            // Only allow SELECT statements
            if (!(statement instanceof Select)) {
                throw new IllegalArgumentException("Only SELECT queries are allowed");
            }

            // Convert to string and check for potentially dangerous operations
            String normalizedSQL = statement.toString().toUpperCase();

            // Verify query only accesses allowed tables
            verifyAllowedTables(normalizedSQL);

        } catch (JSQLParserException e) {
            throw new IllegalArgumentException("Failed to parse SQL query: " + e.getMessage());
        }
    }

    private String extractSQLFromResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty response from AI service");
        }

        // Trim and remove any leading/trailing whitespace
        String cleanedResponse = response.trim();

        // Predefined patterns to extract SQL query
        Pattern[] sqlPatterns = {
                // Pattern to match entire SQL query, including nested queries
                Pattern.compile("(SELECT\\s+.*)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
        };

        for (Pattern pattern : sqlPatterns) {
            Matcher matcher = pattern.matcher(cleanedResponse);
            if (matcher.find()) {
                String extractedQuery = matcher.group(1).trim();

                // Ensure query has proper termination
                if (!extractedQuery.endsWith(";")) {
                    extractedQuery += ";";
                }

                // Add LIMIT if not present


                logger.info("Extracted SQL Query: {}", extractedQuery);
                return extractedQuery;
            }
        }

        // If no pattern matches, use a more aggressive extraction
        Pattern fallbackPattern = Pattern.compile("SELECT.*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher fallbackMatcher = fallbackPattern.matcher(cleanedResponse);

        if (fallbackMatcher.find()) {
            String extractedQuery = fallbackMatcher.group().trim();

            // Ensure query has proper termination
            if (!extractedQuery.endsWith(";")) {
                extractedQuery += ";";
            }

            // Add LIMIT if not present
            if (!extractedQuery.toUpperCase().contains("LIMIT")) {
                extractedQuery = extractedQuery.substring(0, extractedQuery.length() - 1) + " LIMIT 100;";
            }

            logger.info("Fallback Extracted SQL Query: {}", extractedQuery);
            return extractedQuery;
        }

        throw new IllegalArgumentException("Unable to extract valid SELECT query from response: " + cleanedResponse);
    }

    private void verifyAllowedTables(String normalizedSQL) {
        List<String> extractedTables = new ArrayList<>();
        for (String allowedTable : allowedTables) {
            if (normalizedSQL.contains(allowedTable)) {
                extractedTables.add(allowedTable);
            }
        }

        if (extractedTables.isEmpty()) {
            throw new IllegalArgumentException("Query doesn't reference any allowed tables");
        }
    }
}