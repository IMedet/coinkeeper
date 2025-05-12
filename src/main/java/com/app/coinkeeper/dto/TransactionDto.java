package com.app.coinkeeper.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private Long transactionDate;
    private Long categoryId;
    private String type; // "INCOME" or "EXPENSE"
}