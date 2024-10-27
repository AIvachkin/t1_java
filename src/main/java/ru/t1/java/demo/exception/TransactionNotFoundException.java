package ru.t1.java.demo.exception;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message, Long transactionId) {
        super(message);
    }
}