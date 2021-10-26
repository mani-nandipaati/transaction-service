package com.assessment.example.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.assessment.example.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(final long accountNumber);
}

