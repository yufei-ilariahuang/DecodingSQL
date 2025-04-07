package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Composite primary key class for Grade entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradePK implements Serializable {

    private String sessionId;
    private LocalDateTime createdTime;
}