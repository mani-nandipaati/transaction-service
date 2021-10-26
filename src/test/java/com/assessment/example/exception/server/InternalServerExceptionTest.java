package com.assessment.example.exception.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InternalServerExceptionTest {

    @Test
    void test_getErrorCode() {
        final InternalServerException serverException = new InternalServerException("Internal server error.");
        assertEquals("SERVER_ERROR", serverException.getErrorCode());
    }
}
