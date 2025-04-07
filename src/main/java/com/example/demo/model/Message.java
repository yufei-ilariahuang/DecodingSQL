package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "MESSAGE")
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    //
    @Id
    @Column(name = "message_id")
    private String id;

    @Column(name = "sender_id", nullable = false)
    private String senderId;
    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDateTime.now();
        }
    }

    public void setCreatedTime(Date date) {
    }
}
