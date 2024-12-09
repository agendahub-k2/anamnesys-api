package com.anamnesys.exception;

import com.anamnesys.util.Constants;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException() {
        super(Constants.TEMPLATE_NOT_FOUND);
    }
}
