package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Patient;

public interface PersonneRepository extends CrudRepository<Patient, Long> {

}
