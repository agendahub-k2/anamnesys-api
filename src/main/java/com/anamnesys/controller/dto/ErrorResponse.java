package com.anamnesys.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ErrorResponse {
    private String message;
    private List<ValidationError> errors;


    public ErrorResponse(String message, List<ValidationError> errors) {
        this.message = message;
        this.errors = errors;
    }

    @Setter
    @Getter
    @ToString
    public static class ValidationError {
        private String field;
        private String error;

        public ValidationError(String field, String error) {
            this.field = field;
            this.error = error;
        }

    }
}
