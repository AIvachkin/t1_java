package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.service.ClientService;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ClientService clientService;

    @PostMapping("/{accountId}/unblock")
    public ResponseEntity<String> unblockAccount(@PathVariable Long accountId) {
        boolean unblocked = clientService.unblockAccount(accountId);
        if (unblocked) {
            return ResponseEntity.ok("Account unblocked successfully");
        } else {
            return ResponseEntity.badRequest().body("Unable to unblock account");
        }
    }
}