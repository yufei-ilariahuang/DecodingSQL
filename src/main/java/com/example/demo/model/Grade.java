package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "GRADE")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GradePK.class)
public class Grade {

    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Id
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(nullable = false)
    private String grade;

    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDateTime.now();
        }
    }
}