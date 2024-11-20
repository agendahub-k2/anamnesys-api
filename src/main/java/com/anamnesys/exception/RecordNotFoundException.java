package com.anamnesys.exception;

import com.anamnesys.util.Constants;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {
        super(Constants.RECORD_NOT_FOUND);
    }
}
