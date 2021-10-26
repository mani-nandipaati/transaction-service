package com.assessment.example.mapper;

import org.springframework.stereotype.Component;

import com.assessment.example.model.Account;
import com.assessment.example.request.AccountRequest;


@Component
public class AccountRequestMapper implements RequestMapper<AccountRequest, Account> {

    @Override
    public Account mapFrom(AccountRequest request) {
        return Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .balance(request.getBalance())
                .build();
    }
}
