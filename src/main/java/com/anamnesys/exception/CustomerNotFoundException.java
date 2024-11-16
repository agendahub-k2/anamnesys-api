package com.anamnesys.exception;

import com.anamnesys.util.Constants;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException() {
        super(Constants.CUSTOMER_NOT_FOUND);
    }
}
