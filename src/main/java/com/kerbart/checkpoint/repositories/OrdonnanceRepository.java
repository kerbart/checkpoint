package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Ordonnance;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    Ordonnance findByToken(String token);
    //
    // @Query("SELECT p FROM Patient p WHERE p.application.token =
    // :applicationToken")
    // List<Patient> findAllByApplication(@Param("applicationToken") String
    // applicationToken);
}
