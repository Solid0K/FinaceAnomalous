package com.krishu.finaceanomoly.CustomException;

public class InvalidDecisionException extends RuntimeException {
    public InvalidDecisionException(String message) {
        super(message);
    }
}
