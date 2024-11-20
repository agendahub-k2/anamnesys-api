package com.anamnesys.exception;

import com.anamnesys.util.Constants;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super(Constants.PATIENT_NOT_FOUND);
    }
}
