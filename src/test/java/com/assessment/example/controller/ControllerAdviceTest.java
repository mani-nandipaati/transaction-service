package com.assessment.example.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.assessment.example.controller.AccountControllerTest.ACCOUNT_NUMBER;
import static com.assessment.example.utils.TestUtils.asJsonString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.assessment.example.exception.client.AccountNotFoundException;
import com.assessment.example.exception.client.BadRequestException;
import com.assessment.example.exception.server.InternalServerException;
import com.assessment.example.request.AccountRequest;
import com.assessment.example.request.MoneyTransactionRequest;
import com.assessment.example.service.AccountService;
import com.assessment.example.service.MoneyTransactionService;

import lombok.SneakyThrows;

@WebMvcTest({AccountController.class, MoneyTransactionController.class})
class ControllerAdviceTest {

    public static final String SOMETHING_WENT_BADLY_WRONG = "Something went badly wrong.";
    public static final String SERVER_ERROR = "SERVER_ERROR";
    public static final String CLIENT_ERROR = "CLIENT_ERROR";
    public static final String SOMETHING_CLIENT_HAS_SENT_BAD = "Something client has sent bad.";
    public static final String ACCOUNT_NOT_FOUND = "Account not found.";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private MoneyTransactionService moneyTransactionService;

    @SneakyThrows
    @Test
    void internalServerExceptionTest() {
        when(accountService.retrieve(anyLong())).thenThrow(new InternalServerException(SOMETHING_WENT_BADLY_WRONG));

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", ACCOUNT_NUMBER)
                .accept(APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(SERVER_ERROR))
                .andExpect(jsonPath("$.error").value(SOMETHING_WENT_BADLY_WRONG));
    }

    @SneakyThrows
    @Test
    void badRequestExceptionTest() {
        when(accountService.retrieve(anyLong())).thenThrow(new BadRequestException(SOMETHING_CLIENT_HAS_SENT_BAD));

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", ACCOUNT_NUMBER).accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.error").value(SOMETHING_CLIENT_HAS_SENT_BAD));
    }

    @SneakyThrows
    @Test
    void accountNotFoundExceptionTest() {
        when(accountService.retrieve(anyLong())).thenThrow(new AccountNotFoundException(ACCOUNT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", ACCOUNT_NUMBER).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.error").value(ACCOUNT_NOT_FOUND));
    }

    @SneakyThrows
    @Test
    void invalidContentTypeTest() {
        mockMvc.perform(get("/api/v1/accounts/{accountNumber}", 100001).accept(APPLICATION_XML))
                .andExpect(status().is4xxClientError());
    }


    @SneakyThrows
    @Test
    void invalidPostAccountDataTest() {
        mockMvc.perform(post("/api/v1/accounts")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(AccountRequest.builder()
                        .build())
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.error", hasSize(3)))
        ;
    }

    @SneakyThrows
    @Test
    void invalidPostTransactionDataTest() {
        mockMvc.perform(post("/api/v1/transactions")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(asJsonString(MoneyTransactionRequest.builder()
                        .build())
                ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(CLIENT_ERROR))
                .andExpect(jsonPath("$.error", hasSize(3)))
        ;
    }

}
