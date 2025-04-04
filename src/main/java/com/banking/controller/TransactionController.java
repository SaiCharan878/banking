package com.banking.controller;

import com.banking.models.Account;
import com.banking.models.Transaction;
import com.banking.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        return ResponseEntity.ok(transactionService.deposit(userId, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        return ResponseEntity.ok(transactionService.withdraw(userId, amount));
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getBalance(userId));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(userId));
    }
}

