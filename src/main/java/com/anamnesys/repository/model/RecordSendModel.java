package com.anamnesys.repository.model;

import com.anamnesys.domain.STATUS_RECORD;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private STATUS_RECORD status;

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

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "return_dt", nullable = true)
    private LocalDateTime returnDt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }
}

