package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "TRAINING_SESSION")
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSession {

    @Id
    @Column(name = "session_id")
    private String id;

    @Column(name = "student_ids")
    private String studentIds;

    @Column(name = "problem_ids")
    private String problemIds;

    @Column(name = "created_time")
    private LocalDate createdTime;

    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDate.now();
        }
    }
}
