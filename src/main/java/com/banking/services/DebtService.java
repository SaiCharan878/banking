package com.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.models.Debt;
import com.banking.repositories.DebtRepository;

import java.util.List;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    // Save a new debt
    public Debt saveDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    // Get debts for a user
    public List<Debt> getDebtsByUserId(Long userId) {
        return debtRepository.findByUserId(userId);
    }
}

