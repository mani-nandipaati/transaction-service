package com.assessment.example.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.request.MoneyTransactionRequest;


@ExtendWith(MockitoExtension.class)
class MoneyTransactionRequestMapperTest {

    private final MoneyTransactionRequestMapper moneyTransactionRequestMapper = new MoneyTransactionRequestMapper();

    @Test
    void testMapping() {
        final MoneyTransactionRequest request = MoneyTransactionRequest.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .referenceNotes("Monthly Rent")
                .transactionAmount(new BigDecimal("1000"))
                .build();
        final MoneyTransaction transaction = moneyTransactionRequestMapper.mapFrom(request);

        assertNotNull(transaction);
        assertEquals(request.getFromAccountNumber(), transaction.getFromAccountNumber());
        assertEquals(request.getToAccountNumber(), transaction.getToAccountNumber());
        assertEquals(request.getReferenceNotes(), transaction.getReferenceNotes());
        assertEquals(request.getTransactionAmount(), transaction.getTransactionAmount());
        assertFalse(transaction.getTransactionId() > 0);
    }
}