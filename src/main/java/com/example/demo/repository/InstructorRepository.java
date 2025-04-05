package com.example.demo.repository;

import com.example.demo.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, String> {
    Optional<Instructor> findByEmail(String email);
    Optional<Instructor> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}