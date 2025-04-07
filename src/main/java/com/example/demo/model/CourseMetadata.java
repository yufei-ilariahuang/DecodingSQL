package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "COURSE_METADATA")
@NoArgsConstructor
@AllArgsConstructor
public class CourseMetadata {

    @Id
    @Column(name = "session_id")
    private String sessionId;  // References TRAINING_SESSION.session_id

    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "instructor_id")
    private String instructorId;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "description")
    private String description;
}