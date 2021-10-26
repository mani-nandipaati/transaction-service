package com.assessment.example.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.request.MoneyTransactionRequest;
import com.assessment.example.service.MoneyTransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/transactions")
@Tag(name = "Money Transfer API", description = "This API is used to send money from one account to another account.")
public class MoneyTransactionController {

    private final MoneyTransactionService moneyTransactionService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MoneyTransaction transferMoney(@Valid @RequestBody final MoneyTransactionRequest moneyTransactionRequest) {
        log.debug("Money transaction action.");
        return moneyTransactionService.transferMoney(moneyTransactionRequest);
    }
    
    @GetMapping(value = "/{accountNumber}", produces = APPLICATION_JSON_VALUE)
    public List<MoneyTransaction> getTransactionByAccountNumber(@PathVariable @Min(value = 1000001) final long accountNumber) {
        log.debug("Fetching transaction with {}", accountNumber);
        return moneyTransactionService.getTransactionByAccountNumber(accountNumber);
    }

}
