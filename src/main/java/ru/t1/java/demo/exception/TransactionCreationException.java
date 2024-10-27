package ru.t1.java.demo.exception;

public class TransactionCreationException extends RuntimeException {

    public TransactionCreationException(String message, Throwable ex) {
        super(message, ex);
    }
}