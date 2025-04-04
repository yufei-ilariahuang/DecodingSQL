package com.example.demo.dto;

import com.example.demo.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private String introContent;

    // Static factory method
    public static StudentDto fromEntity(Student student) {

        if (student == null) return null;

        return StudentDto.builder()
                .id(student.getStudentId())
                .username(student.getUsername())
                .email(student.getEmail())
                .introContent(student.getIntroContent())
                .build();
    }

    // Conversion to entity
    public Student toEntity() {
        Student student = new Student();
        student.setStudentId(this.id);
        student.setUsername(this.username);
        student.setEmail(this.email);
        student.setPassword(this.password);
        student.setIntroContent(this.introContent);
        // Only set password if it's actually provided
        if (this.password != null && !this.password.isEmpty()) {
            student.setPassword(this.password);
        }
        return student;
    }
}
