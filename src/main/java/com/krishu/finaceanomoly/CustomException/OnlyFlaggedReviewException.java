package com.krishu.finaceanomoly.CustomException;

public class OnlyFlaggedReviewException extends RuntimeException {
    public OnlyFlaggedReviewException(String message) {
        super(message);
    }
}
