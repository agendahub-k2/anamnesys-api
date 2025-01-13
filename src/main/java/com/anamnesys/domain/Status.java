package com.anamnesys.domain;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("P"),
    SENT("S"),
    FAILED("F"),
    CANCELLED("C");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}