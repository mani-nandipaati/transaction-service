package com.assessment.example.exception.client;

import com.assessment.example.exception.StandardError;

public class ClientException extends RuntimeException implements StandardError {

	private static final long serialVersionUID = -1913129320897693049L;

	public ClientException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "CLIENT_ERROR";
    }
}

