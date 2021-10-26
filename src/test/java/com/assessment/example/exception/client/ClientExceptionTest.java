package com.assessment.example.exception.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClientExceptionTest {

    @Test
    void test_getErrorCode() {
        final ClientException clientException = new ClientException("Client Error");
        assertEquals("CLIENT_ERROR", clientException.getErrorCode());
    }
}