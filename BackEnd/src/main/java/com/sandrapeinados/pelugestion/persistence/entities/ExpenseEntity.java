package com.sandrapeinados.pelugestion.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exp_id")
    private long id;
    @Column(name = "exp_title")
    private String expenseTitle;
    @Column(name = "exp_description")
    private String expenseDescription;
    @Column(name = "exp_amount")
    private double expenseAmount;
    @Column(name = "exp_date")
    private LocalDateTime date;

}
