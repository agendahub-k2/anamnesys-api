package com.anamnesys.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PatientRequest {

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 40, min = 4, message = "Nome should be between 5 and 40 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email should not be longer than 100 characters")
    private String email;

    @NotBlank(message = "phone cannot be empty or null")
    @Size(max = 15, message = "Telefone should not be longer than 15 characters")
    private String phone;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birth;

}
