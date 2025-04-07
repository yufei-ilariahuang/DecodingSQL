package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "SQL_PROBLEM")
@NoArgsConstructor
@AllArgsConstructor
public class SqlProblem {

    @Id
    @Column(name = "problem_id")
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "difficulty_level", nullable = false)
    private String difficultyLevel;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (title == null || title.isEmpty()) {
            title = "Problem (" + createdAt + ")";
        }
    }

    public enum DifficultyLevel {
        Easy, Medium, Hard
    }

}
