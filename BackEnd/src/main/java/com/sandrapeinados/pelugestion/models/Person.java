package com.sandrapeinados.pelugestion.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {

    private long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    private String surname;
    private String cellphone;

}
