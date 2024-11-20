package com.anamnesys.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionRequest {

    private Long id;

    @NotBlank(message = "Question cannot be empty or null")
    private String question;

    @NotBlank(message = "Category cannot be empty or null")
    private Long section;

}
