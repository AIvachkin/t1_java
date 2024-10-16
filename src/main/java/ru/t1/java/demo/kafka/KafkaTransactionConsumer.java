package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {

    private final TransactionService transactionService;

    @KafkaListener(id = "${t1.kafka.consumer.transaction-group-id}",
            topics = "${t1.kafka.topic.transactions}",
            containerFactory = "transactionListenerContainerFactory")
    public void listener(@Payload List<Transaction> transactions,
                         Acknowledgment ack) {
        log.debug("Transaction consumer: Обработка новых сообщений");

        try {
            transactionService.saveTransactions(transactions);
        } finally {
            ack.acknowledge();
        }
    }
}