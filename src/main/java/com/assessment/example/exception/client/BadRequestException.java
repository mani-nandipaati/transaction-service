package com.assessment.example.exception.client;

public class BadRequestException extends ClientException {

	private static final long serialVersionUID = -1774574508719835014L;

	public BadRequestException(String message) {
        super(message);
    }
}