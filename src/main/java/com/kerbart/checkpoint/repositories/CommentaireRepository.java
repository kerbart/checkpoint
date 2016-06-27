package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.Commentaire;

public interface CommentaireRepository extends CrudRepository<Commentaire, Long> {
    
    @Query("SELECT c FROM Commentaire c WHERE c.patient.token = :patientToken ORDER BY c.dateCreation")
    List<Commentaire> findByPatientToken(@Param("patientToken") String patientToken);
}
