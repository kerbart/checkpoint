package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    Patient findByToken(String token);

    @Query("SELECT p FROM Patient p WHERE p.cabinet.token = :cabinetToken")
    List<Patient> findAllByCabinet(@Param("cabinetToken") String cabinetToken);
}
