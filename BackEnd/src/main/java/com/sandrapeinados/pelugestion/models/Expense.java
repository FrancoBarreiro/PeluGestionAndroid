package com.sandrapeinados.pelugestion.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    private long id;
    @NotBlank(message = "Debe contener un título.")
    private String expenseTitle;
    private String expenseDescription;
    @Positive(message = "Digite un número mayor a 0.")
    @NotBlank(message = "El monto no puede estar vacío")
    private double expenseAmount;
    @NotBlank(message = "La fecha no puede estar vacia")
    private String date;
}
