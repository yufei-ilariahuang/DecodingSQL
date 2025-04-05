package com.example.demo.service;

import com.example.demo.dto.InstructorDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Instructor;
import com.example.demo.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    public InstructorDto getInstructor(String id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
        return convertToDto(instructor);
    }

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public InstructorDto getInstructorByUsername(String username) {
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with username: " + username));
        return convertToDto(instructor);
    }

    public InstructorDto getInstructorByEmail(String email) {
        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with email: " + email));
        return convertToDto(instructor);
    }

    // Helper method to convert Instructor entity to InstructorDto
    private InstructorDto convertToDto(Instructor instructor) {
        return InstructorDto.builder()
                .id(instructor.getInstructorId())
                .username(instructor.getUsername())
                .email(instructor.getEmail())
                .introContent(instructor.getIntroContent())
                // Don't include password in DTO for security
                .build();
    }

    @Transactional
    public InstructorDto createInstructor(InstructorDto instructorDto) {
        // Check if username or email already exists
        if (instructorRepository.existsByUsername(instructorDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (instructorRepository.existsByEmail(instructorDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Create new instructor
        Instructor instructor = new Instructor();

        // Generate an instructor ID if none provided
        if (instructorDto.getId() == null || instructorDto.getId().isEmpty()) {
            instructor.setInstructorId("instructor_" + UUID.randomUUID().toString().substring(0, 6));
        } else {
            instructor.setInstructorId(instructorDto.getId());
        }

        instructor.setUsername(instructorDto.getUsername());
        instructor.setEmail(instructorDto.getEmail());
        instructor.setPassword(instructorDto.getPassword());
        instructor.setIntroContent(instructorDto.getIntroContent());

        Instructor savedInstructor = instructorRepository.save(instructor);
        return convertToDto(savedInstructor);
    }

    @Transactional
    public InstructorDto updateInstructor(String id, InstructorDto instructorDto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));

        // Check if username is being changed and if it's already taken
        if (!instructor.getUsername().equals(instructorDto.getUsername())
                && instructorRepository.existsByUsername(instructorDto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Check if email is being changed and if it's already taken
        if (!instructor.getEmail().equals(instructorDto.getEmail())
                && instructorRepository.existsByEmail(instructorDto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Update fields
        instructor.setUsername(instructorDto.getUsername());
        instructor.setEmail(instructorDto.getEmail());
        if (instructorDto.getPassword() != null && !instructorDto.getPassword().isEmpty()) {
            instructor.setPassword(instructorDto.getPassword());
        }
        instructor.setIntroContent(instructorDto.getIntroContent());

        Instructor updatedInstructor = instructorRepository.save(instructor);
        return convertToDto(updatedInstructor);
    }

    @Transactional
    public void deleteInstructor(String id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
        instructorRepository.delete(instructor);
    }

    /**
     * Authenticates an instructor by email and password
     *
     * @param email The instructor's email
     * @param password The instructor's password (plain text for now)
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticateInstructor(String email, String password) {
        try {
            Instructor instructor = instructorRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with email: " + email));
            // In a real application, you should use a password encoder
            return instructor.getPassword().equals(password);
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    /**
     * Logs in an instructor and returns their data
     *
     * @param email The instructor's email
     * @param password The instructor's password
     * @return InstructorDto if login successful
     * @throws IllegalArgumentException if credentials are invalid
     */
    public InstructorDto loginInstructor(String email, String password) {
        if (authenticateInstructor(email, password)) {
            return getInstructorByEmail(email);
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}