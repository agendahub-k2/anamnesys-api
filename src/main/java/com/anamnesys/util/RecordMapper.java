package com.anamnesys.util;


import com.anamnesys.controller.dto.QuestionResponse;
import com.anamnesys.controller.dto.RecordRequest;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.RecordModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecordMapper {

    public static RecordModel toModel(RecordRequest request, Long userId, Long recordId) {

        RecordModel model = new RecordModel();
        model.setId(recordId);
        model.setDescription(request.getDescription());
        model.setName(request.getName());
        model.setUserId(userId);
        model.setSegment(SegmentMapper.getSegment(request.getSegment()));
        model.setTemplateId(request.getTemplateId());

        List<QuestionModel> questionsModel =
                Optional.ofNullable(request.getQuestions())
                        .orElse(new ArrayList<>())
                        .stream()
                        .map(it -> {
                            QuestionModel questionModel = new QuestionModel();
                            questionModel.setQuestion(it.getQuestion());
                            questionModel.setSection(it.getSection());
                            questionModel.setDescriptionSection(it.getDescriptionSection());
                            questionModel.setIsRequired(it.getIsRequired());
                            questionModel.setQuestionType(it.getQuestionType());
                            questionModel.setRecord(model);
                            return questionModel;

                        }).collect(Collectors.toList());

        model.setQuestions(questionsModel);

        return model;
    }

    public static RecordResponse toRecordResponse(RecordModel model) {
        RecordResponse response = new RecordResponse();
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setUpdateAt(model.getUpdateAt().toString());
        response.setId(model.getId());
        response.setName(model.getName());
        response.setUserId(model.getUserId());
        response.setDescription(model.getDescription());
        response.setDescription(model.getDescription());
        response.setSegment(SegmentMapper.getSegment(model.getSegment()));

        List<QuestionResponse> question = model.getQuestions().stream().map(it -> {
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

        response.setQuestions(question);

        return response;
    }
}
