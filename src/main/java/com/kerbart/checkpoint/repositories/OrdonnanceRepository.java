package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.Ordonnance;

public interface OrdonnanceRepository extends CrudRepository<Ordonnance, Long> {

    Ordonnance findByToken(String token);
    
    @Query("SELECT o FROM Ordonnance o WHERE o.patient.token = :patientToken ORDER BY o.dateDebut")
    List<Ordonnance> findByPatientToken(@Param("patientToken") String patientToken);
}
