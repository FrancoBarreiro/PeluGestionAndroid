package com.sandrapeinados.pelugestion.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubJob {

    private long id;
    @NotBlank(message = "Establezca un título")
    private String subJobTitle;
    @Positive(message = "Digite un número mayor a 0.")
    @NotBlank(message = "El precio no puede estar vacío")
    private double subJobAmount;
}
