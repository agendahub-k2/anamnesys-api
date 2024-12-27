package com.anamnesys.util;

import com.anamnesys.controller.dto.QuestionResponse;
import com.anamnesys.controller.dto.TemplateResponse;
import com.anamnesys.controller.dto.TemplatesBySegmentResponse;
import com.anamnesys.repository.model.TemplateModel;

import java.util.*;
import java.util.stream.Collectors;

import static com.anamnesys.util.SegmentMapper.getSegment;

public class TemplateMapper {


    public static TemplatesBySegmentResponse toResponse(List<TemplateModel> models) {

        Map<String, List<TemplateResponse>> templatesBySegment = models.stream()
                .collect(Collectors.groupingBy(
                        template -> template.getSegment().getName(),
                        Collectors.mapping(template -> {
                            // Mapear TemplateModel para TemplateResponse
                            TemplateResponse templateResponse = new TemplateResponse();
                            templateResponse.setId(template.getId());
                            templateResponse.setName(template.getName());
                            templateResponse.setDescription(template.getDescription());
                            templateResponse.setSegment(getSegment(template.getSegment()));
                            return templateResponse;
                        }, Collectors.toList())
                ));


        TemplatesBySegmentResponse response = new TemplatesBySegmentResponse();
        response.setTemplates(templatesBySegment);

        return response;
    }

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
            String options = it.getOptions();
            List<String> optionsList = (options != null && !options.isEmpty())
                    ? new ArrayList<>(Arrays.asList(options.split(";")))
                    : new ArrayList<>();
            questionResponse.setOptions(optionsList);
            return questionResponse;
        }).toList();
    }
}
