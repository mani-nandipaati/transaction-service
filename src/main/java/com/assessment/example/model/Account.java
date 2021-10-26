package com.assessment.example.model;

import java.math.BigDecimal;

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
@EqualsAndHashCode(of = {"accountNumber"})
@Entity
@SequenceGenerator(name="account_number_seq", initialValue=1000001, allocationSize=100)
public class Account {
	
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_number_seq")
    private long accountNumber;
    
    private String firstName;
    
    private String lastName;
    
    private BigDecimal balance;
    
}

