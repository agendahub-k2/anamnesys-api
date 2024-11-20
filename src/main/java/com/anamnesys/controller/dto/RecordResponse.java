package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecordResponse {

    private Long id;
    private String name;
    private String createdAt;
    private String updateAt;
    private Long userId;
    private List<QuestionResponse> questions;
}


