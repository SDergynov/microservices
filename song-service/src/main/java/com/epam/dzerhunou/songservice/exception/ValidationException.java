package com.epam.dzerhunou.songservice.exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final int status;
    private final Map<String, String> details;

    public ValidationException(String message, int status, Map<String, String> details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
