package com.sandrapeinados.pelugestion.persistence.repositories;

import com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICustomerRepository extends JpaRepository<CustomerEntity, Long> {

    @EntityGraph(attributePaths = "jobs")
    List<CustomerEntity> findAll();
    @EntityGraph(attributePaths = "jobs")
    Page<CustomerEntity> findAll(Pageable pageable);

    @Query("SELECT new com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity(c.id, c.name, c.surname, c.cellphone) FROM CustomerEntity c WHERE c.id =?1")
    Optional<CustomerEntity> getOnlyDetailsCustomer(Long id);

    @Transactional
    @Modifying
    @Query(value = "Update people Set name =?1, surname=?2, cellphone=?3 Where person_id =?4", nativeQuery = true)
    void updateCustomer(String name, String surname, String cellphone, Long id);

    @Query("SELECT new com.sandrapeinados.pelugestion.persistence.entities.CustomerEntity(c.id, c.name, c.surname, c.cellphone) FROM CustomerEntity c WHERE UPPER(c.name) LIKE ?1")
    List<CustomerEntity> getCustomersByName(String name);
}
