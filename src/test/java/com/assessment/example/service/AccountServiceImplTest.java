package com.assessment.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.assessment.example.exception.client.AccountNotFoundException;
import com.assessment.example.exception.client.BadRequestException;
import com.assessment.example.exception.server.InternalServerException;
import com.assessment.example.mapper.RequestMapper;
import com.assessment.example.model.Account;
import com.assessment.example.repo.AccountRepository;
import com.assessment.example.request.AccountRequest;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RequestMapper<AccountRequest, Account> accountRequestMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void fetchAccount() {
        final long accountNumber = 1000001;
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(
                        Account.builder()
                                .accountNumber(accountNumber)
                                .balance(new BigDecimal("1000.00"))
                                .lastName("Bevan")
                                .firstName("Martin")
                                .build()
                );

        final Account account = accountService.retrieve(accountNumber);

        assertNotNull(account);
        assertEquals(accountNumber, account.getAccountNumber());
    }

    @Test
    void invalidAccount() {
        when(accountRepository.findByAccountNumber(anyLong())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> accountService.retrieve(12343));
    }

    @Test
    void createAccount() {
        final Account dummyAccount = Account.builder()
                .balance(new BigDecimal("1000.00"))
                .lastName("Bevan")
                .firstName("Martin")
                .build();
        when(accountRequestMapper.mapFrom(any(AccountRequest.class)))
                .thenReturn(dummyAccount);
        when(accountRepository.save(any(Account.class)))
                .thenReturn(dummyAccount.toBuilder().accountNumber(10000012).build());

        final Account account = accountService.createAccount(mock(AccountRequest.class));

        assertNotNull(account);
        assertEquals(10000012, account.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertEquals("Martin", account.getFirstName());
        assertEquals("Bevan", account.getLastName());
    }

    @Test
    void internalException() {
        when(accountRequestMapper.mapFrom(any(AccountRequest.class)))
                .thenReturn(null);

        assertThrows(InternalServerException.class, () -> accountService.createAccount(mock(AccountRequest.class)));
    }

    @Test
    void fundsTransfer() {
        //Assemble
        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");
        final Account fromAccount = Account.builder()
                .accountNumber(fromAccountNumber)
                .firstName("Paul")
                .lastName("Smith")
                .balance(new BigDecimal("1000"))
                .build();

        final Account toAccount = Account.builder()
                .accountNumber(toAccountNumber)
                .firstName("Firstname2")
                .lastName("Lastname2")
                .balance(new BigDecimal("1000"))
                .build();

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.findByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        when(accountRepository.findByAccountNumber(toAccountNumber)).thenReturn(toAccount);
        when(accountRepository.saveAll(argThat(accounts -> {
            assertNotNull(accounts);
            final Iterator<Account> iterable = accounts.iterator();
            assertTrue(iterable.hasNext());
            assertEquals(new BigDecimal("900"), iterable.next().getBalance());
            assertTrue(iterable.hasNext());
            assertEquals(new BigDecimal("1100"), iterable.next().getBalance());
            return true;
        }))).thenReturn(List.of(fromAccount, toAccount));

        accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund);
    }

    @Test
    void insufficientFunds() {
        //Insufficient funds in fromAccount

        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");
        final Account fromAccount = Account.builder()
                .accountNumber(fromAccountNumber)
                .firstName("Paul")
                .lastName("Smith")
                .balance(new BigDecimal("50"))
                .build();

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.TRUE);
        when(accountRepository.findByAccountNumber(fromAccountNumber)).thenReturn(fromAccount);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        assertThrows(InternalServerException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void invalidFromAccount() {
        //Invalid fromAccount (meaning: not available in the system)

        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.FALSE);
        verify(accountRepository, never()).existsById(toAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void invalidToAccount() {
        //Invalid toAccount (meaning: not available in the system)

        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("100");

        when(accountRepository.existsById(fromAccountNumber)).thenReturn(Boolean.TRUE);
        verify(accountRepository, atMostOnce()).existsById(toAccountNumber);
        when(accountRepository.existsById(toAccountNumber)).thenReturn(Boolean.FALSE);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }

    @Test
    void invalidTransferAmount() {
        //Invalid Money Transfer value

        final long fromAccountNumber = 10001;
        final long toAccountNumber = 10002;
        final BigDecimal fund = new BigDecimal("-100");

        verify(accountRepository, never()).existsById(fromAccountNumber);
        verify(accountRepository, never()).existsById(toAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(fromAccountNumber);
        verify(accountRepository, never()).findByAccountNumber(toAccountNumber);
        verify(accountRepository, never()).saveAll(anyIterable());

        assertThrows(BadRequestException.class,
                () -> accountService.makeFundTransfer(fromAccountNumber, toAccountNumber, fund));
    }
}
