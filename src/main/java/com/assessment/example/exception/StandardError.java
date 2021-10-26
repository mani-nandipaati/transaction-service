package com.assessment.example.exception;

import java.util.List;

import com.assessment.example.response.ErrorMessage;


public interface StandardError {
    String getErrorCode();
    String getMessage();

    default ErrorMessage getErrorMessage() {
        return new ErrorMessage(getErrorCode(), List.of(getMessage()));
    }
}