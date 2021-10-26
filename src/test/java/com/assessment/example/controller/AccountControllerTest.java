package com.assessment.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assessment.example.utils.TestUtils.asJsonString;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.assessment.example.model.Account;
import com.assessment.example.request.AccountRequest;
import com.assessment.example.service.AccountService;

import lombok.SneakyThrows;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    public static final String FIRST_NAME = "Martin";
    public static final String LAST_NAME = "Bevan";
    public static final String MONEY_BALANCE = "9999";
    public static final long ACCOUNT_NUMBER = 10000010;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @SneakyThrows
    @Test
    void retreiveAccount() {
        final Account account = Account.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .balance(new BigDecimal(MONEY_BALANCE))
                .build();
        when(accountService.retrieve(anyLong())).thenReturn(account);

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", ACCOUNT_NUMBER).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balance").value(MONEY_BALANCE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER));
    }

    @SneakyThrows
    @Test
    void createAccount() {
        final Account account = Account.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .balance(new BigDecimal(MONEY_BALANCE))
                .build();
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(account);

        mockMvc.perform(post("/api/v1/accounts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(AccountRequest.builder()
                        .firstName(FIRST_NAME)
                        .lastName(LAST_NAME)
                        .balance(new BigDecimal(MONEY_BALANCE))
                        .build())
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.balance").value(MONEY_BALANCE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER));
    }
}