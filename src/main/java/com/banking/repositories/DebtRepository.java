package com.banking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.models.Debt;

import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByUserId(Long userId);
}
