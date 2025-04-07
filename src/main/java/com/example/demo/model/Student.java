package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STUDENT")
//@PrimaryKeyJoinColumn(name = "student_id")
public class Student{

    @Id
    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "intro_content", columnDefinition = "TEXT")
    private String introContent;

    // Helper method to get ID for consistency with mappers
    public String getId() {
        return studentId;
    }

    // Helper method to set ID for consistency with mappers
    public void setId(String id) {
        this.studentId = id;
    }

    @PrePersist
    protected void onCreate() {
        if (introContent == null || introContent.isEmpty()) {
            introContent = "Hi, " + username;
        }
    }


}
