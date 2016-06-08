package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.TourneeOccurence;

public interface TourneeOccurenceRepository extends CrudRepository<TourneeOccurence, Long> {

    TourneeOccurence findByToken(String token);

}
