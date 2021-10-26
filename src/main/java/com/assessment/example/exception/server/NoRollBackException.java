package com.assessment.example.exception.server;

public class NoRollBackException extends InternalServerException {

	private static final long serialVersionUID = -1566019611250454640L;

	public NoRollBackException(final String message) {
        super(message);
    }
}