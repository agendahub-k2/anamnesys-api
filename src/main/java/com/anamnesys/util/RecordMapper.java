package com.anamnesys.util;


import com.anamnesys.controller.dto.QuestionResponse;
import com.anamnesys.controller.dto.RecordRequest;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.controller.dto.SendRecordRequest;
import com.anamnesys.domain.SendRecord;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.RecordModel;
import com.anamnesys.repository.model.RecordSendModel;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
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
                            questionModel.setOptions(String.join(";", it.getOptions()));
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
            questionResponse.setCreatedAt(it.getCreatedAt().toString());
            questionResponse.setOptions(new ArrayList<>(Collections.singletonList(it.getOptions())));
            return questionResponse;
        }).toList();

        response.setQuestions(question);

        return response;
    }

    public static RecordResponse toRecordNotQuestionsResponse(RecordModel model) {
        RecordResponse response = new RecordResponse();
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setUpdateAt(model.getUpdateAt().toString());
        response.setId(model.getId());
        response.setName(model.getName());
        response.setUserId(model.getUserId());
        response.setDescription(model.getDescription());
        response.setDescription(model.getDescription());
        response.setSegment(SegmentMapper.getSegment(model.getSegment()));

        return response;
    }

    public static SendRecord toSendRecord(SendRecordRequest sendRecordRequest, Long userId) {
        SendRecord sendRecord = new SendRecord();
        sendRecord.setUserId(userId);
        sendRecord.setClientId(sendRecordRequest.getClientId());
        sendRecord.setMessage(sendRecordRequest.getMessage());
        sendRecord.setIsSendMail(sendRecordRequest.getIsSendMail());
        sendRecord.setIsSendWhatsapp(sendRecordRequest.getIsSendWhatsapp());
        sendRecord.setRecordId(sendRecordRequest.getRecordId());
        sendRecord.setDateExpiration(sendRecordRequest.getDateExpiration());
        sendRecord.setDateExpiration(sendRecordRequest.getDateExpiration());

        return sendRecord;
    }

    public static RecordSendModel toSendModel(SendRecord sendRecord) {
        RecordSendModel recordSendModel = new RecordSendModel();
        recordSendModel.setId(sendRecord.getId().toString());
        recordSendModel.setClientId(sendRecord.getClientId());
        recordSendModel.setMessage(sendRecord.getMessage());
        recordSendModel.setIsSendMail(sendRecord.getIsSendMail());
        recordSendModel.setIsSendWhatsapp(sendRecord.getIsSendWhatsapp());
        recordSendModel.setRecordId(sendRecord.getRecordId());
        recordSendModel.setDateExpiration(sendRecord.getDateExpiration());
        recordSendModel.setUserId(sendRecord.getUserId());
        return recordSendModel;
    }
}
