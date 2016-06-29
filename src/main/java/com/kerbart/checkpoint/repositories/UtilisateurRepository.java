package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.Utilisateur;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    Utilisateur findByEmail(String email);

    Utilisateur findByToken(String token);
    
    @Query("SELECT ua.utilisateur FROM UtilisateurApplication ua WHERE ua.application.token = :appToken" )
    List<Utilisateur> findByAppToken(@Param("appToken") String appToken);
}
