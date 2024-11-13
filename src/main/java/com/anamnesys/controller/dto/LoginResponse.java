package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
public class    LoginResponse extends UserResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
}
