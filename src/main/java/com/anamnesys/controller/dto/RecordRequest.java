package com.anamnesys.controller.dto;

import com.anamnesys.controller.validations.ValidTemplateIdQuestions;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ValidTemplateIdQuestions
public class RecordRequest {

    private Long id;

    private Long templateId;

    @NotBlank(message = "Name cannot be empty or null")
    @Size(max = 400, min = 4, message = "Name should be between 4 and 40 characters")
    private String name;

    @NotBlank(message = "description cannot be empty or null")
    @Size(max = 400, min = 4, message = "Descrição error - between 4 and 40 characters")
    private String description;

    @Valid
    @NotNull(message = "Segmento não pode ser vazia.")
    private SegmentRequest segment;

    private List<QuestionRequest> questions;

}