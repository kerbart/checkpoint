package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Application;

public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Application findByToken(String token);

}
