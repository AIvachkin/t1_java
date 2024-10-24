package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAccountConsumer {

    private final AccountService accountService;

    @KafkaListener(id = "${t1.kafka.consumer.account-group-id}",
            topics = "${t1.kafka.topic.accounts}",
            containerFactory = "accountListenerContainerFactory")
    public void listener(@Payload List<Account> accounts,
                         Acknowledgment ack) {
        log.debug("Account consumer: Обработка новых сообщений");

        try {
            accountService.saveAccounts(accounts);
        } finally {
            ack.acknowledge();
        }
    }
}