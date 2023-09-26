package com.sandrapeinados.pelugestion.services;


import com.sandrapeinados.pelugestion.models.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IExpenseService {

    Expense saveExpense(Expense expense);
    List<Expense> getExpenses();
    Expense getExpenseById(Long id);
    void deleteExpense(Long id);
    Expense updateExpense(Expense expense);
    Page<Expense> getExpensesPaged(Pageable pageable);
    Page<Expense> findExpensesBetweenDates(String desde, String hasta, int page, int size);
    double getSumTotalJobsByDates(String dateFrom, String dateTo);


}
