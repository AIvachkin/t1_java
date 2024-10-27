package ru.t1.java.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

    private Long id;
    private Long accountId;
    private Long clientId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private String description;
    private boolean isCancellation;
    private Long cancelledTransactionId;

    public static TransactionDto fromTransaction(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccountId())
                .clientId(transaction.getClientId())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .timestamp(transaction.getTimestamp())
                .description(transaction.getDescription())
                .isCancellation(transaction.isCancellation())
                .cancelledTransactionId(transaction.getCancelledTransactionId())
                .build();
    }
}