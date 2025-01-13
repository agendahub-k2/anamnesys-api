package com.anamnesys.repository.model;

import com.anamnesys.domain.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "SEND_RECORD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordSendModel {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    private LocalDateTime dateExpiration;

    @Column(name = "message", length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 1, nullable = false)
    private Status status;

    @Column(name = "whatsapp", nullable = false)
    private Boolean isSendWhatsapp;

    @Column(name = "email", nullable = false)
    private Boolean isSendMail;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

