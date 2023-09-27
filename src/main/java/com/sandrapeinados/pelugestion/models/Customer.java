package com.sandrapeinados.pelugestion.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer extends Person {

    private List<Job> jobs;

    public Customer(long id, String name, String surname, String cellphone) {
        super(id, name, surname, cellphone);
    }

}
