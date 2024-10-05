package com.kirana.register.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final ErrorMessageLoader errorMessageLoader;

    public GlobalExceptionHandler(ErrorMessageLoader errorMessageLoader) {
        this.errorMessageLoader = errorMessageLoader;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        logger.error("Error occurred: {}", ex.getMessage());

        String errorCode = ex.getErrorCode();
        String description = errorMessageLoader.getDescription(errorCode);
        String message = errorMessageLoader.getMessage(errorCode);

        ErrorResponse errorResponse = new ErrorResponse(errorCode, description, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Internal server error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("500", "Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
