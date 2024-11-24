package com.anamnesys.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SegmentRequest{

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 400, min = 4, message = "Name should be between 4 and 40 characters")
    private String name;

    private String description;
}
