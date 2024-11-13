package com.anamnesys.controller.dto;

import com.anamnesys.util.STATUS;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String category;
    private String createdAt;
    private String updateAt;
    private STATUS status;
}
