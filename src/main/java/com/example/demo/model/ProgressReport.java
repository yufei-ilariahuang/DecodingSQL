package com.example.demo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PROGRESS_REPORT")
@NoArgsConstructor
@AllArgsConstructor
public class ProgressReport {

    @Id
    @Column(name = "report_id")
    private String id;

    @Column(name = "student_id", nullable = false)
    private String studentId;


    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "report_content", nullable = false, columnDefinition = "TEXT")
    private String reportContent;

    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDateTime.now();
        }

        if (reportContent == null || reportContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Report content cannot be empty");
        }
    }
}
