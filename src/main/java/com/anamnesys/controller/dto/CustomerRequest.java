package com.anamnesys.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 40, min = 5, message = "Name should be between 5 and 40 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email should not be longer than 100 characters")
    private String email;

    @NotBlank(message = "phone cannot be empty or null")
    @Size(max = 15, message = "Phone should not be longer than 15 characters")
    private String phone;


}
