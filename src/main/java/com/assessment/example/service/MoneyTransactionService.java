package com.assessment.example.service;

import java.util.List;

import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.request.MoneyTransactionRequest;

public interface MoneyTransactionService {
	MoneyTransaction transferMoney(final MoneyTransactionRequest moneyTxReq);
    List<MoneyTransaction> getTransactionByAccountNumber(final long accountNumber);
}
