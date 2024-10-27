package ru.t1.java.demo.service;

import ru.t1.java.demo.model.ErrorTransaction;

public interface CorrectionService {

    void handleErrorTransaction(ErrorTransaction errorTransaction);

    void periodicRetry();
}