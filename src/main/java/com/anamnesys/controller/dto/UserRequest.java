package com.anamnesys.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 40, min = 3, message = "Nome inválido - between 3 and 15 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email should not be longer than 100 characters")
    private String email;

    @NotBlank(message = "phone cannot be empty or null")
    @Size(max = 11, min = 11, message = "Telefone inválido")
    private String phone;

    @NotBlank(message = "Password cannot be empty or null")
    @Size(min = 4, max = 15, message = "Senha inválida - between 4 and 15 characters")
    private String password;

    @NotBlank(message = "Category cannot be empty or null")
    @Size(min = 4, max = 50, message = "Categoria inválida - between 4 and 15 characters")
    private String category;

}

