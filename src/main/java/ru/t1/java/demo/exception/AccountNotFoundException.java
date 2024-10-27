package ru.t1.java.demo.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message, Long accountId) {
        super(message);
    }
}