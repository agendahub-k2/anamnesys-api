package com.anamnesys.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordResponse {

    private Long id;
    private String name;
    private String description;
    private SegmentResponse segment;
    private String createdAt;
    private String updateAt;
    private Long userId;
    private TermResponse term;
    private List<QuestionResponse> questions;
}