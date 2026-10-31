
package com.example.finsecure_pay;

import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private final TransactionValidator validator;

    public TransactionController(TransactionValidator validator) {
        this.validator = validator;
    }

    @PostMapping("/validate")
    public ValidationResult validateTransaction(
            @RequestBody TransactionRequest request) {

        return validator.validate(request);
    }
}
