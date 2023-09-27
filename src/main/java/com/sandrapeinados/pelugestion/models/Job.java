package com.sandrapeinados.pelugestion.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Job {

    private List<SubJob> subJobs;
    private long idJob;
    @NonNull
    private Long idClient;
    @NonNull
    @NotBlank(message = "El titulo no puede estar vacío")
    private String jobTitle;
    private String jobDescription;
    @NonNull
    @NotBlank
    @Positive(message = "Total debe ser mayor a 0.")
    private Double totalAmount;
    @NonNull
    private String date;
    private String customerName;
    private String customerSurname;
}
