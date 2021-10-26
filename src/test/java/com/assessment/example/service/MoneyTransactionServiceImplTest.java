package com.assessment.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assessment.example.exception.server.InternalServerException;
import com.assessment.example.exception.server.NoRollBackException;
import com.assessment.example.mapper.RequestMapper;
import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.repo.MoneyTransactionRepository;
import com.assessment.example.request.MoneyTransactionRequest;

@ExtendWith(MockitoExtension.class)
class MoneyTransactionServiceImplTest {

    @Mock
    private MoneyTransactionRepository moneyTransactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private RequestMapper<MoneyTransactionRequest, MoneyTransaction> moneyTransactionRequestMapper;

    @InjectMocks
    private MoneyTransactionServiceImpl moneyTransactionService;


    @Test
    void transferAmount() {
        final MoneyTransaction moneyTransaction = MoneyTransaction.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .transactionAmount(new BigDecimal("200"))
                .referenceNotes("Monthly Rent")
                .build();
        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(moneyTransaction);
        doNothing().when(accountService).makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, atMostOnce()).save(moneyTransaction);
        when(moneyTransactionRepository.save(moneyTransaction))
                .thenAnswer(invocationOnMock -> {
                    final MoneyTransaction receivedTransaction = invocationOnMock.getArgument(0);
                    receivedTransaction.setTransactionId(1000001);
                    return receivedTransaction;
                });

        final MoneyTransaction transaction = moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class));

        assertNotNull(transaction);
        assertEquals(1000001, transaction.getTransactionId());
        assertEquals("SUCCESS", transaction.getStatus());
        assertNotNull(transaction.getReferenceNotes());
    }

    @Test
    void insufficientFunds() {
        //AccountService's makeFundTransfer function throws exception

        final MoneyTransaction moneyTransaction = MoneyTransaction.builder()
                .fromAccountNumber(1000010)
                .toAccountNumber(1000020)
                .transactionAmount(new BigDecimal("200"))
                .referenceNotes("Monthly Rent")
                .build();
        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(moneyTransaction);
        doThrow(new InternalServerException("Insufficient fund.")).when(accountService)
                .makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, atMostOnce()).save(moneyTransaction);
        when(moneyTransactionRepository.save(moneyTransaction))
                .thenAnswer(invocationOnMock -> {
                    final MoneyTransaction receivedTransaction = invocationOnMock.getArgument(0);
                    assertNotNull(receivedTransaction);
                    assertEquals("FAILED", receivedTransaction.getStatus());
                    assertEquals("Insufficient fund.", receivedTransaction.getErrorDescription());
                    return receivedTransaction;
                });

        assertThrows(NoRollBackException.class, () -> moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class)));

    }

    @Test
    void internalServerException() {
        //moneyTransactionRequestMapper fails to produce entity

        when(moneyTransactionRequestMapper.mapFrom(any(MoneyTransactionRequest.class)))
                .thenReturn(null);
        verify(accountService, never()).makeFundTransfer(anyLong(), anyLong(), any(BigDecimal.class));
        verify(moneyTransactionRepository, never()).save(any(MoneyTransaction.class));

        assertThrows(InternalServerException.class, () -> moneyTransactionService
                .transferMoney(mock(MoneyTransactionRequest.class)));

    }
}