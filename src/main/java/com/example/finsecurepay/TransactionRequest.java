package com.example.finsecure_pay;

import java.math.BigDecimal;

public record TransactionRequest(
    String transactionId,
    String accountId,
    BigDecimal amount,
    String currency
)
{}