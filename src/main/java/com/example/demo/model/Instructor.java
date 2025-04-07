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
@Table(name = "INSTRUCTOR")
public class Instructor {
    @Id
    @Column(name = "instructor_id")
    private String instructorId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "intro_content")
    private String introContent;

    // Helper method to get ID for consistency with mappers
    public String getId() {
        return instructorId;
    }

    // Helper method to set ID for consistency with mappers
    public void setId(String id) {
        this.instructorId = id;
    }

    @PrePersist
    protected void onCreate() {
        if (introContent == null || introContent.isEmpty()) {
            introContent = "Hi, I am instructor " + username;
        }
    }
}
