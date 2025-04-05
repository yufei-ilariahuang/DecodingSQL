package com.example.demo.dto;

import com.example.demo.model.Instructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDto {
    private String id;
    private String username;
    private String email;
    private String password;  // Only used for request, never in response
    private String introContent;

    // Static method to convert from entity to DTO
    public static InstructorDto fromEntity(Instructor instructor) {
        return InstructorDto.builder()
                .id(instructor.getInstructorId())
                .username(instructor.getUsername())
                .email(instructor.getEmail())
                .introContent(instructor.getIntroContent())
                // Don't include password for security
                .build();
    }
}