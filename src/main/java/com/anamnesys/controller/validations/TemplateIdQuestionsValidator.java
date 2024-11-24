package com.anamnesys.controller.validations;

import com.anamnesys.controller.dto.RecordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TemplateIdQuestionsValidator implements ConstraintValidator<ValidTemplateIdQuestions, RecordRequest> {

    @Override
    public boolean isValid(RecordRequest recordRequest, ConstraintValidatorContext context) {
        if (recordRequest == null) {
            return true; // Não validar objetos nulos
        }

        if (recordRequest.getTemplateId() != null) {
            // Se templateId não for nulo, questions deve ser nulo
            return recordRequest.getQuestions() == null || recordRequest.getQuestions().isEmpty();
        } else {
            // Se templateId for nulo, questions deve ser não vazia
            return false;
        }
    }
}

