package com.anamnesys.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateResponse {

    private Long id;
    private String name;
    private SegmentResponse segment;
    private String description;
    private List<QuestionResponse> questions;

}

