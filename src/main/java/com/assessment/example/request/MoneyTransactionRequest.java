package com.assessment.example.request;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransactionRequest {

    @Min(value = 1000001)
    private long fromAccountNumber;
    
    @Min(value = 1000001)
    private long toAccountNumber;
    
    @NotNull
    @DecimalMin("1.00")
    private BigDecimal transactionAmount;
    
    @Size(max = 100)
    private String referenceNotes;
}