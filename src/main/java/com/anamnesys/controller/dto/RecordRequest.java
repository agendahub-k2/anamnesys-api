package com.anamnesys.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecordRequest {

    private Long id;

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 40, min = 4, message = "Name should be between 4 and 40 characters")
    private String name;

    @NotEmpty(message = "A lista de perguntas não pode ser vazia.")
    @Valid
    private List<QuestionRequest> questions;

}
