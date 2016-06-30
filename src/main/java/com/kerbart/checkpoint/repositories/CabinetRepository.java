package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Cabinet;

public interface CabinetRepository extends CrudRepository<Cabinet, Long> {

    Cabinet findByToken(String token);

    Cabinet findByShortCode(String shortCode);
}
