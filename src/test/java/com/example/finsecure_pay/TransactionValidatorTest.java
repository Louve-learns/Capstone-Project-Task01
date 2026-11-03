
package com.example.finsecure_pay;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class TransactionValidatorTest {

    TransactionValidator validator = new TransactionValidator();

    // Test 1: Valid transaction
    @Test
    void testValidTransaction() {

        TransactionRequest request = new TransactionRequest(
            "TXN001", "ACC001", new BigDecimal("100.00"), "SGD"
        );

        ValidationResult result = validator.validate(request);

        assertTrue(result.valid());
    }

    // Test 2: Negative amount
    @Test
    void testNegativeAmount() {

        TransactionRequest request = new TransactionRequest(
            "TXN002", "ACC002", new BigDecimal("-50.00"), "SGD"
        );

        ValidationResult result = validator.validate(request);

        assertFalse(result.valid());
    }

    // Test 3: Zero amount
    @Test
    void testZeroAmount() {

        TransactionRequest request = new TransactionRequest(
            "TXN003", "ACC003", BigDecimal.ZERO, "SGD"
        );

        ValidationResult result = validator.validate(request);

        assertFalse(result.valid());
    }

    // Test 4: Missing account ID
    @Test
    void testMissingAccount() {

        TransactionRequest request = new TransactionRequest(
            "TXN004", "", new BigDecimal("100.00"), "SGD"
        );

        ValidationResult result = validator.validate(request);

        assertFalse(result.valid());
    }

    // Test 5: Unsupported currency
    @Test
    void testUnsupportedCurrency() {

        TransactionRequest request = new TransactionRequest(
            "TXN005", "ACC005", new BigDecimal("100.00"), "USD"
        );

        ValidationResult result = validator.validate(request);

        assertFalse(result.valid());
    }

    // Test 6: Missing transaction ID
    @Test
    void testMissingTransactionId() {

        TransactionRequest request = new TransactionRequest(
            "", "ACC006", new BigDecimal("100.00"), "SGD"
        );

        ValidationResult result = validator.validate(request);
        

        assertFalse(result.valid());
    }
     

}
