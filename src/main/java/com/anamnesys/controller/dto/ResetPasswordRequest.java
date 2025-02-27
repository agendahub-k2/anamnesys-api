package com.anamnesys.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Old password cannot be empty or null")
    @Size(max = 100, message = "Old password should not be longer than 100 characters")
    private String oldPassword;

    @NotBlank(message = "Password cannot be empty or null")
    @Size(max = 100, message = "Password should not be longer than 100 characters")
    private String newPassword;
}
