package com.anamnesys.exception;


import com.anamnesys.util.Constants;

import java.text.MessageFormat;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String email, String token) {
        super(MessageFormat.format("{0} email: {1} token: {2}", Constants.UNAUTHORIZED, email, token));
    }

    public UnauthorizedException(String token) {
        super(MessageFormat.format("{0} token: {1}", Constants.UNAUTHORIZED, token));
    }

    public UnauthorizedException() {
        super(Constants.UNAUTHORIZED);
    }
}
