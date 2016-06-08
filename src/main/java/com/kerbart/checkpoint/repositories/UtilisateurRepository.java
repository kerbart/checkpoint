package com.kerbart.checkpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import com.kerbart.checkpoint.model.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    Utilisateur findByEmail(String email);

    Utilisateur findByToken(String token);
}
