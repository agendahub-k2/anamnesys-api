package com.anamnesys.controller.dto;

import com.anamnesys.util.STATUS;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String createdAt;
    private String updateAt;
    private Long userId;
}
