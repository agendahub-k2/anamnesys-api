package com.anamnesys.util;

import com.anamnesys.controller.dto.QuestionResponse;
import com.anamnesys.controller.dto.TemplateResponse;
import com.anamnesys.repository.model.TemplateModel;

import java.util.List;

public class TemplateMapper {


    public static TemplateResponse toResponse(TemplateModel model) {
        TemplateResponse response = new TemplateResponse();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setDescription(model.getDescription());
        response.setSegment(SegmentMapper.getSegment(model.getSegment()));
        response.setQuestions(getQuestions(model));

        return response;
    }

    private static List<QuestionResponse> getQuestions(TemplateModel model) {

        return model.getQuestions().stream().map(it -> {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setId(it.getId());
            questionResponse.setQuestion(it.getQuestion());
            questionResponse.setSection(it.getSection());
            questionResponse.setDescriptionSection(it.getDescriptionSection());
            questionResponse.setIsRequired(it.getIsRequired());
            questionResponse.setQuestionType(it.getQuestionType());
            questionResponse.setCreatedAt(it.getCreatedAt().toString());
            questionResponse.setUpdateAt(it.getUpdateAt().toString());
            return questionResponse;
        }).toList();
    }
}
