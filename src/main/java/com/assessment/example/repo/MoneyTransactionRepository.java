package com.assessment.example.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.example.model.MoneyTransaction;

public interface MoneyTransactionRepository extends JpaRepository<MoneyTransaction, Long> {
	List<MoneyTransaction> findByFromAccountNumberOrToAccountNumber(final long fromAccountNumber, final long toAccountNumber);
}