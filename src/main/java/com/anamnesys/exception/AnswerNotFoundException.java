package com.anamnesys.exception;

import com.anamnesys.util.Constants;

public class AnswerNotFoundException extends RuntimeException {
    public AnswerNotFoundException() {
        super(Constants.ANSWER_NOT_FOUND);
    }
}
