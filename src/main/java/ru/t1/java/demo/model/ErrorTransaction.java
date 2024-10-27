package ru.t1.java.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class ErrorTransaction extends Transaction {


    public ErrorTransaction(long id,
                            long accountId,
                            BigDecimal amount,
                            TransactionType transactionType) {
    }
}