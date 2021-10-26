package com.assessment.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "transactionId")
@Entity
@SequenceGenerator(name = "money_transactions_seq", initialValue = 10000001, allocationSize = 100)
public class MoneyTransaction {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "money_transactions_seq")
    private long transactionId;
    
    private long fromAccountNumber;
    
    private long toAccountNumber;
    
    private BigDecimal transactionAmount;
    
    private String referenceNotes;
    
    private String status;
    
    private String errorDescription;
    
    private LocalDateTime createdDate;
    
}

