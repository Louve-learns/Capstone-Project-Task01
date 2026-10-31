
package com.example.finsecure_pay;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {

    public ValidationResult validate(TransactionRequest request) {

        // Rule 1: Transaction details must exist
        if (request == null) {
            return new ValidationResult(false, "Transaction is required");
        }

        // Rule 2: Transaction ID cannot be empty
        if (request.transactionId() == null ||
            request.transactionId().isBlank()) {
            return new ValidationResult(false, "Transaction ID is required");
        }

        // Rule 3: Account ID cannot be empty
        if (request.accountId() == null ||
            request.accountId().isBlank()) {
            return new ValidationResult(false, "Account ID is required");
        }

        // Rule 4: Amount must be greater than 0
        if (request.amount() == null ||
            request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ValidationResult(false, "Amount must be greater than 0");
        }

        // Rule 5: Only SGD is supported
        if (!"SGD".equals(request.currency())) {
            return new ValidationResult(false, "Only SGD is supported");
        }

        // All checks passed
        return new ValidationResult(true, "Transaction is valid");
    }
}
