package com.sandrapeinados.pelugestion.persistence.repositories;

import com.sandrapeinados.pelugestion.persistence.entities.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IJobRepository extends JpaRepository<JobEntity,Long> {

    @EntityGraph(attributePaths = "subJobs")
    List<JobEntity> findAll();

    @EntityGraph(attributePaths = "subJobs")
    Page<JobEntity> findAll(Pageable pageable);

    @Query("SELECT j FROM JobEntity j WHERE j.date BETWEEN :from AND :to")
    Page<JobEntity> findJobsBetweenDates(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query("SELECT j FROM JobEntity j JOIN FETCH j.customerEntity c WHERE j.date BETWEEN :from AND :to")
    Page<JobEntity> findJobsBetweenDatesWithCustomer(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable);

    @Query(value = "SELECT SUM(total_amount) FROM jobs WHERE date BETWEEN :from AND :to",
    nativeQuery = true)
    Optional<Double> getSumTotal(@Param("from") LocalDateTime from, @Param("to")LocalDateTime to);

    @Query("SELECT j FROM JobEntity j WHERE j.customerEntity.id = :id")
    Page<JobEntity> findJobsByCustomerId(@Param("id") Long id, Pageable pageable);
}
