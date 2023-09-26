package com.sandrapeinados.pelugestion.persistence.repositories;

import com.sandrapeinados.pelugestion.persistence.entities.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IExpenseRepository extends JpaRepository<ExpenseEntity,Long> {

    @Query("SELECT e FROM ExpenseEntity e WHERE e.date BETWEEN :from AND :to")
    Page<ExpenseEntity> findExpensesBetweenDates(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query(value = "SELECT SUM(exp_amount) FROM expenses WHERE exp_date BETWEEN :from AND :to",
            nativeQuery = true)
    Optional<Double> getSumTotal(@Param("from") LocalDateTime from, @Param("to")LocalDateTime to);
}
