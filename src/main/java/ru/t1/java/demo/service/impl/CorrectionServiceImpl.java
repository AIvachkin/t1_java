package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.t1.java.demo.model.ErrorTransaction;
import ru.t1.java.demo.model.TransactionType;
import ru.t1.java.demo.service.CorrectionService;
import ru.t1.java.demo.service.TransactionService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorrectionServiceImpl implements CorrectionService {

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;
    private final TransactionService transactionService;

    @Value("${t1.client.service.url}")
    private String clientServiceUrl;

    @KafkaListener(topics = "${t1.kafka.topic.client_transaction_errors}")
    @Override
    public void handleErrorTransaction(ErrorTransaction errorTransaction) {
        if (requestUnblock(errorTransaction.getAccountId())) {
            deleteErrorTransaction(errorTransaction.getId());
        } else {
            insertErrorTransaction(errorTransaction);
        }
    }

    private boolean requestUnblock(Long accountId) {
        try {
            String url = clientServiceUrl + "/api/accounts/" + accountId + "/unblock";
            ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Failed to request unblock for account {}, {}", accountId, e.getMessage());
            return false;
        }
    }

    private void insertErrorTransaction(ErrorTransaction errorTransaction) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO t1_demo_correction (id, account_id, amount, transaction_type) VALUES (?, ?, ?, ?)",
                    errorTransaction.getId(),
                    errorTransaction.getAccountId(),
                    errorTransaction.getAmount(),
                    errorTransaction.getType().toString()
            );
            log.info("Inserted error transaction: {}", errorTransaction);
        } catch (DataAccessException e) {
            log.error("Failed to insert error transaction {}, {}", errorTransaction, e.getMessage());
        }
    }

    private void deleteErrorTransaction(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM t1_demo_correction WHERE id = ?", id);
            log.info("Deleted error transaction with id: {}", id);
        } catch (DataAccessException e) {
            log.error("Failed to delete error transaction with id: {}, {}", id, e.getMessage());
        }
    }

    @Scheduled(fixedDelayString = "${t1.correction.retry.interval}")
    public void periodicRetry() {
        log.info("Starting periodic retry of error transactions");
        List<ErrorTransaction> errorTransactions = fetchErrorTransactions();

        for (ErrorTransaction transaction : errorTransactions) {
            if (requestUnblock(transaction.getAccountId())) {
                deleteErrorTransaction(transaction.getId());
                retryTransaction(transaction);
            }
        }
    }

    private List<ErrorTransaction> fetchErrorTransactions() {
        try {
            return jdbcTemplate.query(
                    "SELECT id, account_id, amount, transaction_type FROM t1_demo_correction",
                    (rs, rowNum) -> new ErrorTransaction(
                            rs.getLong("id"),
                            rs.getLong("account_id"),
                            rs.getBigDecimal("amount"),
                            TransactionType.valueOf(rs.getString("transaction_type"))
                    )
            );
        } catch (DataAccessException e) {
            log.error("Failed to fetch error transactions", e);
            return Collections.emptyList();
        }
    }

    private void retryTransaction(ErrorTransaction errorTransaction) {
        try {
            transactionService.retryTransaction(errorTransaction);
            log.info("Retried transaction: {}", errorTransaction);
        } catch (Exception e) {
            log.error("Failed to retry transaction {}", errorTransaction);
        }
    }
}