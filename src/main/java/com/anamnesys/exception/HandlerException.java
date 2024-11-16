package com.anamnesys.exception;

import com.anamnesys.controller.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class HandlerException {

    private static final Logger logger = LoggerFactory.getLogger(HandlerException.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ValidationError> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return new ErrorResponse.ValidationError(fieldName, errorMessage);
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse("Validation failed", errors);
        logger.info("statusCode {} {} ", HttpStatus.BAD_REQUEST, errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse.ValidationError error = new ErrorResponse.ValidationError("", "");
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), List.of(error));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> exception(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        logger.error("Error {} {} ", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailOrPasswordException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> emailOrPasswordException(EmailOrPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), null);
        logger.warn("{} " +
                "{} ", HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> unauthorizedException(UnauthorizedException ex) {
        logger.info("status code{} {}", HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        String errorMessage = "Erro ao salvar no banco de dados";

        Throwable rootCause = ex.getRootCause();
        while (rootCause != null) {
            if (rootCause instanceof SQLIntegrityConstraintViolationException) {
                errorMessage = "Email já cadastrado.";
                break;
            }
            rootCause = rootCause.getCause();
        }

        ErrorResponse.ValidationError error = new ErrorResponse.ValidationError("", errorMessage);
        ErrorResponse errorResponse = new ErrorResponse(error.getError(), Collections.emptyList());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}

