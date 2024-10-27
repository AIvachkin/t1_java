package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.exception.TransactionCreationException;
import ru.t1.java.demo.model.dto.TransactionRequest;
import ru.t1.java.demo.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> createTransaction(@RequestBody TransactionRequest request) {
        try {
            transactionService.createTransaction(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
        } catch (TransactionCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create transaction");
        }
    }
}
