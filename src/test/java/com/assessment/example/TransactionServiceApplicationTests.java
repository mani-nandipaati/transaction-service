package com.assessment.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.assessment.example.controller.AccountController;
import com.assessment.example.controller.MoneyTransactionController;
import com.assessment.example.repo.AccountRepository;
import com.assessment.example.repo.MoneyTransactionRepository;

@SpringBootTest
class TransactionServiceApplicationTests {

	@Autowired
	private AccountController accountController;
	
	@Autowired
	private MoneyTransactionController moneyTransactionController;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private MoneyTransactionRepository moneyTransactionRepository;
	
	@Test
	void contextLoads() {
		Assertions.assertNotNull(accountController);
		Assertions.assertNotNull(moneyTransactionController);
		Assertions.assertNotNull(accountRepository);
		Assertions.assertNotNull(moneyTransactionRepository);
	}

}
