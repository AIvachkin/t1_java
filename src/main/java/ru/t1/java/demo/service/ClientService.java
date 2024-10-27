package ru.t1.java.demo.service;

import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountType;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.List;

public interface ClientService {
    void registerClients(List<Client> clients);

    Account registerAccount(Long clientId, AccountType accountType);

    void blockDebitAccount(Long accountId);

    boolean unblockAccount(Long accountId);

    void processTransaction(TransactionDto transactionDto);

    List<ClientDto> parseJson();
}
