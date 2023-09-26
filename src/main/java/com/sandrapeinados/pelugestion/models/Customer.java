package com.sandrapeinados.pelugestion.models;

import com.sandrapeinados.pelugestion.persistence.entities.JobEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends Person {

    private List<Job> jobs;

    public Customer(long id, String name, String surname, String cellphone) {
        super(id, name, surname, cellphone);
    }

}
