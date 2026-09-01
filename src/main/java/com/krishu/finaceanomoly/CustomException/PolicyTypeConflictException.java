package com.krishu.finaceanomoly.CustomException;

public class PolicyTypeConflictException extends RuntimeException {
    public PolicyTypeConflictException(String message) {
        super(message);
    }
}
