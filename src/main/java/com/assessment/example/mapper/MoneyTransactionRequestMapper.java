package com.assessment.example.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.request.MoneyTransactionRequest;


@Component
public class MoneyTransactionRequestMapper implements RequestMapper<MoneyTransactionRequest, MoneyTransaction> {

    @Override
    public MoneyTransaction mapFrom(MoneyTransactionRequest request) {
        return MoneyTransaction.builder()
                .fromAccountNumber(request.getFromAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .transactionAmount(request.getTransactionAmount())
                .referenceNotes(request.getReferenceNotes())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
