package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Patient;

public interface PatientRepository extends CrudRepository<Patient, Long> {

    Patient findByToken(String token);
}
