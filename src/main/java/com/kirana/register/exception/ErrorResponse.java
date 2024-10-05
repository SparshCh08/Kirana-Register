package com.kirana.register.exception;

public class ErrorResponse {
    private String errorCode;
    private String description;
    private String message;

    public ErrorResponse(String errorCode, String description, String message) {
        this.errorCode = errorCode;
        this.description = description;
        this.message = message;
    }

    // Getters and Setters
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
