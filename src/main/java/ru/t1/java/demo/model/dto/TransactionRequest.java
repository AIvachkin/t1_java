package ru.t1.java.demo.model.dto;

import lombok.Data;
import ru.t1.java.demo.model.TransactionType;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private Long accountId;
    private BigDecimal amount;
    private TransactionType type;
}
