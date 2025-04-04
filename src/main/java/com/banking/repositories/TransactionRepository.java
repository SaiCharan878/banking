package com.banking.repositories;

import com.banking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    
    List<Transaction> findByAccountIdOrderByTimestampDesc(Long accountId);
}

