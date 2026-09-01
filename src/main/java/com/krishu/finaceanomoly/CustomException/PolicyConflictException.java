package com.krishu.finaceanomoly.CustomException;

public class PolicyConflictException extends RuntimeException {
    public PolicyConflictException(String message) {
        super(message);
    }
}
