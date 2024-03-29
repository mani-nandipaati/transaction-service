package com.assessment.example.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.assessment.example.exception.client.BadRequestException;
import com.assessment.example.exception.client.AccountNotFoundException;
import com.assessment.example.exception.server.InternalServerException;
import com.assessment.example.exception.server.NoRollBackException;
import com.assessment.example.response.ErrorMessage;

@RestControllerAdvice
public class ControllerAdvice {

    public static final String CLIENT_ERROR = "CLIENT_ERROR";

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage badRequestErrorHandler(final BadRequestException exception) {
        return exception.getErrorMessage();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ErrorMessage notFoundErrorHandler(final AccountNotFoundException exception) {
        return exception.getErrorMessage();
    }

    @ExceptionHandler({InternalServerException.class, NoRollBackException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    ErrorMessage internalServerErrorHandler(final InternalServerException exception) {
        return exception.getErrorMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage pathParamValidatorConstraintErrorHandler(final ConstraintViolationException exception) {
        return ErrorMessage.builder()
                .errorCode(CLIENT_ERROR)
                .error(exception
                        .getConstraintViolations()
                        .stream()
                        .map(violation -> String.format("%s: %s",
                                StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                        .map(Path.Node::getName)
                                        .reduce((s, s2) -> s2)
                                        .orElse("unknown"),
                                violation.getMessage()))
                        .collect(Collectors.toList())
                )
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    ErrorMessage bodyValidatorConstraintErrorHandler(final MethodArgumentNotValidException exception) {
        return ErrorMessage.builder()
                .errorCode(CLIENT_ERROR)
                .error(exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> String.format("%s: %s",
                                fieldError.getField(),
                                fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build();
    }
}

