package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.*;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.ClientService;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.t1.java.demo.model.AccountType.*;
import static ru.t1.java.demo.model.TransactionStatus.*;
import static ru.t1.java.demo.model.TransactionType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaClientProducer kafkaClientProducer;

    @Value("${t1.kafka.topic.client_transaction_errors}")
    private String transactionErrorTopic;

    //        Кредитный лимит в 1000 единиц взят в качестве примера, т.к. не хватает вводных данных о том,
    //        каким он должен быть (или как определяться)
    private final BigDecimal CREDIT_LIMIT_INITIAL = BigDecimal.valueOf(1000);

    @Override
    public void registerClients(List<Client> clients) {
        clientRepository.saveAll(clients);
    }

    @Override
    public Account registerAccount(Long clientId, AccountType accountType) {
        Account account = Account.builder()
                .clientId(clientId)
                .accountType(accountType)
                .creditLimit(Objects.equals(CREDIT, accountType) ? CREDIT_LIMIT_INITIAL : null)
                .isBlocked(false)
                .build();

        return accountRepository.save(account);
    }

    @Override
    public void blockDebitAccount(Long accountId) {
        Account account = getAccountById(accountId);
        account.setBlocked(true);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public boolean unblockAccount(Long accountId) {
        Account account = getAccountById(accountId);

        if (Objects.equals(account.getAccountType(), CREDIT)) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
                account.setBlocked(false);
                accountRepository.save(account);
                reprocessPendingTransactions(account);
                return true;
            }
            return false;
        } else if (Objects.equals(DEBIT, account.getAccountType())) {
            account.setBlocked(false);
            accountRepository.save(account);
            return true;
        }

        return false;
    }

    @Override
    public void processTransaction(TransactionDto transactionDto) {
        Account account = getAccountById(transactionDto.getAccountId());

        if (account.isBlocked()) {
            kafkaClientProducer.sendTo(transactionErrorTopic, transactionDto.getId());
            saveAsPendingTransaction(transactionDto);
            return;
        }

        Transaction transaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .accountId(transactionDto.getAccountId())
                .type(transactionDto.getType())
                .clientId(account.getClientId())
                .build();

        if (Objects.equals(CANCEL, transactionDto.getType())) {
            cancelTransaction(transaction);
        } else {
            processNewTransaction(transaction, account);
        }
    }

    private void saveAsPendingTransaction(TransactionDto transactionDto) {
        Transaction pendingTransaction = Transaction.builder()
                .accountId(transactionDto.getAccountId())
                .amount(transactionDto.getAmount())
                .type(transactionDto.getType())
                .status(PENDING)
                .build();
        transactionRepository.save(pendingTransaction);
    }

    private void reprocessPendingTransactions(Account account) {
        List<Transaction> pendingTransactions = transactionRepository.findPendingTransactionsByAccountId(account.getId());
        for (Transaction transaction : pendingTransactions) {
            processTransaction(TransactionDto.fromTransaction(transaction));
        }
    }

    private void cancelTransaction(Transaction transaction) {
        Transaction existingTransaction = transactionRepository.findById(Objects.requireNonNull(transaction.getId()))
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id {} not found", transaction.getId()));

        Account account = getAccountById(existingTransaction.getAccountId());

        account.setBalance(account.getBalance().subtract(existingTransaction.getAmount()));
        accountRepository.save(account);
        transactionRepository.delete(existingTransaction);
    }

    private void processNewTransaction(Transaction transaction, Account account) {
        BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
        account.setBalance(newBalance);

        if (Objects.equals(account.getAccountType(), CREDIT)
                && newBalance.compareTo(BigDecimal.ZERO) < 0) {
            account.setBlocked(true);
        }

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    //    @Transactional
    @Override
    public List<ClientDto> parseJson() {
        ObjectMapper mapper = new ObjectMapper();

        ClientDto[] clients = new ClientDto[0];
        try {
            clients = mapper.readValue(new File("src/main/resources/MOCK_DATA.json"), ClientDto[].class);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            log.warn("Exception: ", e);
        }

        return Arrays.asList(clients);
    }

    private Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with id {} not found", accountId));
    }
}
