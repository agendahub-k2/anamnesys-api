package com.anamnesys.util;

import com.anamnesys.controller.dto.PatientRequest;
import com.anamnesys.controller.dto.PatientResponse;
import com.anamnesys.repository.model.PatientModel;

public class PatientMapper {

    public static PatientModel toModel(PatientRequest request, Long patientId, Long userId) {
        PatientModel patientModel = new PatientModel();
        patientModel.setId(patientId);
        patientModel.setName(request.getName());
        patientModel.setPhone(request.getPhone());
        patientModel.setEmail(request.getEmail());
        patientModel.setUserId(userId);
        patientModel.setBirth(request.getBirth());
        return patientModel;
    }

    public static PatientResponse toUserResponse(PatientModel model) {
        PatientResponse response = new PatientResponse();
        response.setName(model.getName());
        response.setEmail(model.getEmail());
        response.setPhone(model.getPhone());
        response.setId(model.getId());
        response.setUpdateAt(model.getUpdateAt().toString());
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setUserId(model.getUserId());
        response.setBirth(model.getBirth());
        return response;
    }
}
