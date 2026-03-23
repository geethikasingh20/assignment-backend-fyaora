package com.gler.assignment.common.exception;

public class UpstreamApiException extends RuntimeException {
    public UpstreamApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
