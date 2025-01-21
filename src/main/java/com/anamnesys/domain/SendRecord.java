package com.anamnesys.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SendRecord {
    
    
    private UUID id =  UUID.randomUUID();
    private Long userId;
    private Long recordId;
    private LocalDateTime dateExpiration;
    private String message;
    private Boolean isSendWhatsapp;
    private Boolean isSendMail;
    private Long clientId;
    private STATUS_RECORD status;
}
