package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.exception.TransactionCreationException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.ErrorTransaction;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.model.dto.TransactionRequest;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final KafkaClientProducer kafkaClientProducer;

    @Value("${t1.kafka.topic.client_transactions}")
    private String clientTransaction;

    @Override
    public void saveTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    @Override
    public void createTransaction(TransactionRequest request) {
        TransactionDto transactionDto = convertToTransactionDto(request);
        sendTransactionToKafka(transactionDto);
    }

    @Override
    public void retryTransaction(ErrorTransaction errorTransaction) {
        try {
            TransactionRequest request = convertErrorTransactionToRequest(errorTransaction);

            createTransaction(request);
            log.info("Retried transaction successfully: {}", errorTransaction);
        } catch (Exception e) {
            log.error("Failed to retry transaction {}: {}", errorTransaction, e.getMessage());
        }
    }

    private TransactionRequest convertErrorTransactionToRequest(ErrorTransaction errorTransaction) {
        TransactionRequest request = new TransactionRequest();
        request.setAccountId(errorTransaction.getAccountId());
        request.setAmount(errorTransaction.getAmount());
        request.setType(errorTransaction.getType());
        return request;
    }

    private TransactionDto convertToTransactionDto(TransactionRequest request) {
        return TransactionDto.builder()
                .accountId(request.getAccountId())
                .amount(request.getAmount())
                .type(request.getType())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private void sendTransactionToKafka(TransactionDto transactionDto) {
        try {
            kafkaClientProducer.sendTo(clientTransaction, transactionDto);
        } catch (Exception e) {
            throw new TransactionCreationException("Failed to send transaction to Kafka", e);
        }
    }
}
