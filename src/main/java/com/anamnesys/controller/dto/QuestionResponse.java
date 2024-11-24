package com.anamnesys.controller.dto;

import com.anamnesys.repository.model.QuestionModel;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionResponse {

    private Long id;
    private String createdAt;
    private String updateAt;
    private String question;
    private Long section;
    private String descriptionSection;
    private Boolean isRequired;
    private QuestionModel.QuestionType questionType;

}
