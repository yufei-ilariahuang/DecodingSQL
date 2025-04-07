package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "PROBLEM_ANSWER")
@NoArgsConstructor
@AllArgsConstructor
public class ProblemAnswer {

    @Id
    @Column(name = "problem_id")
    private String problemId;

    @Column(nullable = false)
    private String answer;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @PrePersist
    protected void onInsert() {
        if (answer != null) {
            answer = answer.toUpperCase();
        }
    }
}