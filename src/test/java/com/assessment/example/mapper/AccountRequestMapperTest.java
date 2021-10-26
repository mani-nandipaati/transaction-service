package com.assessment.example.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assessment.example.model.Account;
import com.assessment.example.request.AccountRequest;


@ExtendWith(MockitoExtension.class)
class AccountRequestMapperTest {

    @InjectMocks
    private AccountRequestMapper accountRequestMapper;

    @Test
    void testMapping() {
        final AccountRequest accountRequest = AccountRequest.builder()
                .firstName("Martin")
                .lastName("Bevan")
                .balance(new BigDecimal("1000"))
                .build();

        final Account account = accountRequestMapper.mapFrom(accountRequest);

        assertNotNull(account);
        assertEquals(accountRequest.getBalance(), account.getBalance());
        assertEquals(accountRequest.getFirstName(), account.getFirstName());
        assertEquals(accountRequest.getLastName(), account.getLastName());
        assertFalse(account.getAccountNumber() > 0);
    }
}
