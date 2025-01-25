package com.anamnesys.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String createdAt;
    private String updateAt;
    private Long userId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birth;
}
