package com.banking.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.converter.TrendsConverter;
import com.banking.dto.DailyExpenseDTO;
import com.banking.dto.MonthlySpending;
import com.banking.dto.TrendsResponse;
import com.banking.services.ExpenseService;

@RestController
@RequestMapping("/api/{userId}/expense")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpenseController {
	
	@Autowired
	ExpenseService expenseService;
	
	@Autowired
	TrendsConverter trendsConverter;
	
	@PostMapping
	@Validated
    public ResponseEntity<String> saveDailyExpense(@RequestBody List<DailyExpenseDTO> dailyExpenseDTOList,
    		@PathVariable(value="userId") @NotNull Long userId) {
        try {
        	System.out.println("DailyExpenseDTO list "+dailyExpenseDTOList);
            expenseService.saveDailyExpense(dailyExpenseDTOList, userId);
            return ResponseEntity.ok("Daily Expense saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save Daily Expense: " + e.getMessage());
        }
    }
	
	@GetMapping
    @Validated
    public ResponseEntity<?> getDailyExpenses(@RequestParam("date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate date,
    		@PathVariable(value="userId") @NotNull Long userId) {
    	try {
    		// Retrieve the expense for the user
            List<DailyExpenseDTO> dailyExpenseDTOList = expenseService.getDailyExpenseByDate(userId, date);
            return ResponseEntity.ok(dailyExpenseDTOList);
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch expense: " + e.getMessage());
    	}
    }
	
	   // API endpoint to get spending trends and suggestions
    @GetMapping("/trends")
    public ResponseEntity<?> getTrends(@PathVariable Long userId) throws Exception {
        TrendsResponse trendsResponse = expenseService.getUserSpendingTrends(userId);
        return ResponseEntity.ok(trendsConverter.convertTrendsResponse(trendsResponse));
    }
    
	  // API endpoint to get dashboard data
	 @GetMapping("/dashboard")
	 public ResponseEntity<?> getDashboardData(@RequestParam("month") int month, @PathVariable Long userId) throws Exception {
	     TrendsResponse trendsResponse = expenseService.getUserSpendingTrends(userId);
	     List<MonthlySpending> monthlySpendingList = trendsConverter.convertTrendsResponse(trendsResponse);
	     MonthlySpending monthlySpendingData = monthlySpendingList.stream().filter(monthlySpending -> monthlySpending.getMonth() == month).findFirst().orElse(null);
	     
	     return ResponseEntity.ok(monthlySpendingData);
	 }

}
