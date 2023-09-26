package com.sandrapeinados.pelugestion.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subJobs")
public class SubJobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subJob_id")
    private long id;
    @Column(name = "subJob_title")
    private String subJobTitle;
    @Column(name = "amount")
    private double subJobAmount;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobEntity job;

    public SubJobEntity(long id, String subJobTitle, double subJobAmount, String jobId) {
        this.id = id;
        this.subJobTitle = subJobTitle;
        this.subJobAmount = subJobAmount;
    }

    @Override
    public String toString() {
        return "SubJobEntity{" +
                "id=" + id +
                ", subJobTitle='" + subJobTitle + '\'' +
                ", subJobAmount=" + subJobAmount +
                '}';
    }
}
