package com.assessment.example.exception.server;

import com.assessment.example.exception.StandardError;

public class InternalServerException extends RuntimeException implements StandardError {

	private static final long serialVersionUID = 4664079330060661122L;

	public InternalServerException(final String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "SERVER_ERROR";
    }
}