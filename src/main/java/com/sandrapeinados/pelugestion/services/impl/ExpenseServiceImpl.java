package com.sandrapeinados.pelugestion.services.impl;

import com.sandrapeinados.pelugestion.exceptions.BadRequestException;
import com.sandrapeinados.pelugestion.exceptions.ResourceNotFoundException;
import com.sandrapeinados.pelugestion.models.Expense;
import com.sandrapeinados.pelugestion.persistence.entities.ExpenseEntity;
import com.sandrapeinados.pelugestion.persistence.repositories.IExpenseRepository;
import com.sandrapeinados.pelugestion.services.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements IExpenseService {
    @Autowired
    private IExpenseRepository expenseRepo;
    String fechaPatron = "dd-MM-yyyy HH:mm:ss";
    DateTimeFormatter formateador = DateTimeFormatter.ofPattern(fechaPatron);

    @Override
    public Expense saveExpense(Expense expense) {


        ExpenseEntity expenseToSave = new ExpenseEntity();
        expenseToSave.setExpenseTitle(expense.getExpenseTitle());
        expenseToSave.setExpenseDescription(expense.getExpenseDescription());
        expenseToSave.setExpenseAmount(expense.getExpenseAmount());
        expenseToSave.setDate(LocalDateTime.parse(expense.getDate(), formateador));
        expenseRepo.save(expenseToSave);
        expense.setId(expenseToSave.getId());
        return expense;
    }

    @Override
    public List<Expense> getExpenses() {
        List<ExpenseEntity> expensesFounds = expenseRepo.findAll();
        List<Expense> expensesList = new ArrayList<>();

        for (ExpenseEntity e : expensesFounds) {
            Expense expense = new Expense();
            expense.setId(e.getId());
            expense.setExpenseTitle(e.getExpenseTitle());
            expense.setExpenseDescription(e.getExpenseDescription());
            expense.setExpenseAmount(e.getExpenseAmount());
            expense.setDate(e.getDate().format(formateador));
            expensesList.add(expense);
        }
        return expensesList;
    }

    @Override
    public Expense getExpenseById(Long id) {
        Optional<ExpenseEntity> expenseFound = expenseRepo.findById(id);
        if (expenseFound.isPresent()) {
            Expense expense = new Expense();
            expense.setId(expenseFound.get().getId());
            expense.setExpenseTitle(expenseFound.get().getExpenseTitle());
            expense.setExpenseDescription(expenseFound.get().getExpenseDescription());
            expense.setExpenseAmount(expenseFound.get().getExpenseAmount());
            expense.setDate(expenseFound.get().getDate().format(formateador));
            return expense;
        } else {
            throw new ResourceNotFoundException("Expense Not Found");
        }
    }

    @Override
    public void deleteExpense(Long id) {
        Optional<ExpenseEntity> expenseFound = expenseRepo.findById(id);
        if (expenseFound.isPresent()) {
            expenseRepo.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Expense not found");
        }
    }

    @Override
    public Expense updateExpense(Expense expense) {
        Optional<ExpenseEntity> expenseFound = expenseRepo.findById(expense.getId());
        if (expenseFound.isPresent()) {
            expenseFound.get().setExpenseTitle(expense.getExpenseTitle());
            expenseFound.get().setExpenseDescription(expense.getExpenseDescription());
            expenseFound.get().setExpenseAmount(expense.getExpenseAmount());
            expenseFound.get().setDate(LocalDateTime.parse(expense.getDate(), formateador));
            expenseRepo.save(expenseFound.get());
            return expense;
        } else {
            throw new ResourceNotFoundException("Expense not found");
        }
    }

    @Override
    public Page<Expense> getExpensesPaged(Pageable pageable) {
        Page<ExpenseEntity> expensesFounds = expenseRepo.findAll(pageable);
        List<Expense> expenses = new ArrayList<>();

        for (ExpenseEntity expenseFound : expensesFounds.getContent()) {
            Expense expense = new Expense();
            expense.setId(expenseFound.getId());
            expense.setExpenseTitle(expenseFound.getExpenseTitle());
            expense.setExpenseDescription(expenseFound.getExpenseDescription());
            expense.setExpenseAmount(expenseFound.getExpenseAmount());
            expense.setDate(expenseFound.getDate().format(formateador));
            expenses.add(expense);
        }
        return new PageImpl<>(expenses, expensesFounds.getPageable(), expensesFounds.getTotalElements());
    }

    @Override
    public Page<Expense> findExpensesBetweenDates(String from, String to, int page, int size) {
        LocalDateTime findFrom = LocalDateTime.parse(from, formateador);
        LocalDateTime findTo = LocalDateTime.parse(to, formateador);
        LocalDate fromDate = findFrom.toLocalDate();
        LocalDate toDate = findTo.toLocalDate();

        Sort sort = Sort.by("date").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        if (fromDate.isBefore(toDate) || fromDate.equals(toDate)) {
            Page<ExpenseEntity> expensesFounds = expenseRepo.findExpensesBetweenDates(findFrom, findTo, pageable);
            List<Expense> expenses = new ArrayList<>();
            for (ExpenseEntity e : expensesFounds.getContent()) {
                Expense expense = new Expense();
                expense.setId(e.getId());
                expense.setExpenseTitle(e.getExpenseTitle());
                expense.setExpenseDescription(e.getExpenseDescription());
                expense.setExpenseAmount(e.getExpenseAmount());
                expense.setDate(e.getDate().format(formateador));
                expenses.add(expense);
            }
            return new PageImpl<>(expenses, expensesFounds.getPageable(), expensesFounds.getTotalElements());
        } else {
            throw new BadRequestException("The 'From' date cannot be greater than the 'To' date.");
        }
    }

    @Override
    public double getSumTotalJobsByDates(String dateFrom, String dateTo) {
        LocalDateTime from = LocalDateTime.parse(dateFrom, formateador);
        LocalDateTime to = LocalDateTime.parse(dateTo, formateador);
        Optional<Double> sumOptional = expenseRepo.getSumTotal(from, to);
        double sum = sumOptional.orElse(0.0);
        return sum;
    }
}
