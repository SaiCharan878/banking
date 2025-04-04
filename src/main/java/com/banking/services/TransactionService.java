package com.banking.services;

import com.banking.models.Account;
import com.banking.models.Transaction;
import com.banking.repositories.AccountRepository;
import com.banking.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction deposit(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }

        Account account = accountRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Account newAccount = new Account();
                    newAccount.setUserId(userId);
                    return accountRepository.save(newAccount);
                });

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Transactional
    public Transaction withdraw(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }

        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType("WITHDRAW");
        transaction.setAmount(amount);
        transactionRepository.save(transaction);

        return transaction;
    }

    public BigDecimal getBalance(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return account.getBalance();
    }

    public List<Transaction> getTransactionHistory(Long userId) {
        Account account = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
        return transactionRepository.findByAccountIdOrderByTimestampDesc(account.getId());
    }
}

