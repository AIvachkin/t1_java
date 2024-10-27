package ru.t1.java.demo.service;

import ru.t1.java.demo.model.ErrorTransaction;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionRequest;

import java.util.List;

public interface TransactionService {

    void saveTransactions(List<Transaction> transactions);

    void createTransaction(TransactionRequest request);

    void retryTransaction(ErrorTransaction errorTransaction);
}
