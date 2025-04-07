package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "SESSION_SUBMISSION")
@NoArgsConstructor
@AllArgsConstructor
public class SessionSubmission {

    @Id
    @Column(name = "submission_id")
    private String id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "problem_id")
    private String problemId;

    @Column(name = "submission_content")
    private String submissionContent;

    @Column(name = "grade")
    private Integer grade;

    @PrePersist
    protected void onCreate() {
        if (submissionContent == null || submissionContent.isEmpty()) {
            throw new IllegalArgumentException("Submission content cannot be empty!");
        }
    }
}