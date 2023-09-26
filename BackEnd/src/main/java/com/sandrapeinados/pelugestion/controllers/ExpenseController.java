package com.sandrapeinados.pelugestion.controllers;

import com.sandrapeinados.pelugestion.models.Expense;
import com.sandrapeinados.pelugestion.services.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> saveExpense(@RequestBody Expense expense) {
        expenseService.saveExpense(expense);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(expense.getId())
                .toUri();
        return ResponseEntity.created(location).body(expense);
    }

    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        List<Expense> expenseList = expenseService.getExpenses();
        return ResponseEntity.ok(expenseList);
    }

    @GetMapping("/paged")
    public ResponseEntity<?> getAllExpensesPaged(@RequestParam int size, @RequestParam int page) {
        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Expense> expensesList = expenseService.getExpensesPaged(pageable);
        return ResponseEntity.ok(expensesList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> updateExpense(@RequestBody Expense expense) {
        return ResponseEntity.ok(expenseService.updateExpense(expense));
    }

    @GetMapping("/results")
    public ResponseEntity<?> getExpensesPageds(@RequestParam int page, @RequestParam int size,
                                               @RequestParam String from, @RequestParam String to) {
        Page<Expense> expensesList = expenseService.findExpensesBetweenDates(from, to, page, size);
        return ResponseEntity.ok(expensesList);
    }

    @GetMapping("/totalByPeriod")
    public ResponseEntity<?> getSumTotal(@RequestParam String from, @RequestParam String to) {
        double sum = expenseService.getSumTotalJobsByDates(from, to);
        return ResponseEntity.ok(sum);
    }

}
