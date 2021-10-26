package com.assessment.example.service;

import java.math.BigDecimal;

import com.assessment.example.model.Account;
import com.assessment.example.request.AccountRequest;

public interface AccountService {
    Account retrieve(final long accountNumber);
    Account createAccount(AccountRequest account);
    void makeFundTransfer(final long fromAccountNumber, final long toAccountNumber, final BigDecimal fund);
}
