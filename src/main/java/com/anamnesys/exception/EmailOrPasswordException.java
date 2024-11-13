package com.anamnesys.exception;


import com.anamnesys.util.Constants;

public class EmailOrPasswordException extends RuntimeException {
    public EmailOrPasswordException() {
        super(Constants.PASSWORD_EMAIL_INVALID);
    }
}

