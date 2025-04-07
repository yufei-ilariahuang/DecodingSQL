package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "GENERATED_ANSWER")
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedAnswer {

    @Id
    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "generated_content", nullable = false)
    private String generatedContent;

    @Column(name = "generated_time", nullable = false)
    private LocalDateTime generatedTime;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @PrePersist
    protected void onCreate() {
        if (generatedTime == null) {
            generatedTime = LocalDateTime.now();
        }
    }
}