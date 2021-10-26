package com.assessment.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.assessment.example.model.MoneyTransaction;
import com.assessment.example.repo.MoneyTransactionRepository;
import com.assessment.example.request.MoneyTransactionRequest;
import com.assessment.example.exception.server.InternalServerException;
import com.assessment.example.exception.server.NoRollBackException;
import com.assessment.example.mapper.RequestMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MoneyTransactionServiceImpl implements MoneyTransactionService {

    private final MoneyTransactionRepository moneyTransactionRepository;
    private final AccountService accountService;
    private final RequestMapper<MoneyTransactionRequest, MoneyTransaction> moneyTransactionRequestMapper;
    
    @Override
    @Transactional(noRollbackFor = NoRollBackException.class)
    public MoneyTransaction transferMoney(final MoneyTransactionRequest moneyTxReq) {

        return Optional.of(moneyTxReq)
                .map(moneyTransactionRequestMapper::mapFrom)
                .map(moneyTransaction -> {
                    final MoneyTransaction outcomeMoneyTransaction;
                    try {
                    	moneyTransaction.setStatus("SUCCESS");
                        accountService
                                .makeFundTransfer(moneyTransaction.getFromAccountNumber(),
                                        moneyTransaction.getToAccountNumber(), moneyTransaction.getTransactionAmount());
                    } catch (Exception e) {
                        moneyTransaction.setStatus("FAILED");
                        moneyTransaction.setErrorDescription(e.getMessage());
                        throw new NoRollBackException(e.getMessage());
                    } finally {
                        outcomeMoneyTransaction = moneyTransactionRepository.save(moneyTransaction);
                    }
                    return outcomeMoneyTransaction;
                })
                .orElseThrow(() -> new InternalServerException("Failure in request conversion."))
                ;
    }
    
    
    public List<MoneyTransaction> getTransactionByAccountNumber(final long accountNumber){
    	return moneyTransactionRepository.findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber);
    }
    

}