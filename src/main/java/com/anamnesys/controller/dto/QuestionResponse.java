package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionResponse {

    private Long id;
    private String createdAt;
    private String updateAt;
    private String question;
    private Long section;

}
