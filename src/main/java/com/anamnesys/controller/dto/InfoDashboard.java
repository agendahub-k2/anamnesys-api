package com.anamnesys.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InfoDashboard {

    private String status;
    private Long clientId;
    private String recordName;
    private LocalDateTime createdAt;
    private boolean email;
    private boolean whatsapp;
    private String patientName;
    private String linkId;
    private LocalDateTime returnDt;
}
