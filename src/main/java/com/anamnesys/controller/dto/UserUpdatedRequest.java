package com.anamnesys.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdatedRequest {

    @Size(max = 40, min = 3, message = "Nome inválido - between 3 and 15 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email should not be longer than 100 characters")
    private String email;

    @Size(max = 11, min = 11, message = "Telefone inválido")
    private String phone;

}
