package com.anamnesys.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AnswerResponseDTO {
    private String answer;
    private LocalDateTime createdAt;
    private String name;
    private String email;
    private String phone;
    private String nameRecord;
    private String nameUser;
}
