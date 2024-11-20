package com.anamnesys.util;


import com.anamnesys.controller.dto.QuestionResponse;
import com.anamnesys.controller.dto.RecordRequest;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.RecordModel;

import java.util.List;

public class RecordMapper {

    public static RecordModel toModel(RecordRequest request, Long userId, Long recordId) {


        RecordModel model = new RecordModel();
        model.setId(recordId);
        model.setName(request.getName());
        model.setUserId(userId);
        List<QuestionModel> questionsModel = request.getQuestions().stream().map(it -> {
            QuestionModel questionModel = new QuestionModel();
            questionModel.setId(it.getId());
            questionModel.setSection(it.getSection());
            questionModel.setQuestion(it.getQuestion());
            questionModel.setRecord(model);
            return questionModel;

        }).toList();

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

        List<QuestionResponse> question = model.getQuestions().stream().map(it -> {
            QuestionResponse questionResponse = new QuestionResponse();
            questionResponse.setSection(it.getSection());
            questionResponse.setId(it.getId());
            questionResponse.setCreatedAt(it.getCreatedAt().toString());
            questionResponse.setUpdateAt(it.getUpdateAt().toString());
            questionResponse.setQuestion(it.getQuestion());

            return questionResponse;
        }).toList();

        response.setQuestions(question);

        return response;
    }
}
