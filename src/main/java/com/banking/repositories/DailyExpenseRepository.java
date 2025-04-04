package com.banking.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banking.dto.MonthlySpending;
import com.banking.models.DailyExpense;
import com.banking.models.User;

@Repository
public interface DailyExpenseRepository extends JpaRepository<DailyExpense, Long> {
    
    List<DailyExpense> findByUserAndExpenseDate(User user, LocalDate date)	;
    
    @Query("SELECT \r\n"
    		+ "    new com.banking.dto.MonthlySpending(MONTH(e.expenseDate), e.category, SUM(e.amount)) \r\n"
    		+ "FROM \r\n"
    		+ "    com.banking.models.DailyExpense e \r\n"
    		+ "WHERE \r\n"
    		+ "    e.user = :user AND YEAR(e.expenseDate) = :year \r\n"
    		+ "GROUP BY \r\n"
    		+ "    MONTH(e.expenseDate), e.category \r\n"
    		+ "ORDER BY \r\n"
    		+ "    MIN(e.expenseDate) ")
    List<MonthlySpending> getMonthWiseSpending(@Param("user") User user, @Param("year") int year);

}

