package com.anamnesys.repository.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ANSWER")
@Getter
@Setter
@NoArgsConstructor
public class AnswerModel {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "answer", columnDefinition = "json", nullable = false)
    private String answer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
