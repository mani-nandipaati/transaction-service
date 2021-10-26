package com.assessment.example.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.assessment.example.model.Account;
import com.assessment.example.request.AccountRequest;
import com.assessment.example.service.AccountService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/accounts")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Account APIs", description = "These APIs are used to create and retrieve customer accounts.")
public class AccountController {

	private final AccountService accountService;
	
	@PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
	public Account createAccount(@Valid @RequestBody final AccountRequest accountRequest) {
		log.debug("Creating an account...");
		return accountService.createAccount(accountRequest);
	}
	
    @GetMapping(value = "/{accountNumber}", produces = APPLICATION_JSON_VALUE)
    public Account getAccountDetail(@PathVariable @Min(value = 1000001) final long accountNumber) {
        log.debug("Fetching account with {}", accountNumber);
        return accountService.retrieve(accountNumber);
    }
}
