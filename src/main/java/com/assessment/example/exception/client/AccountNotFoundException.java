package com.assessment.example.exception.client;


public class AccountNotFoundException extends ClientException {

	private static final long serialVersionUID = -1778837969382968964L;

	public AccountNotFoundException(String message) {
        super(message);
    }
}