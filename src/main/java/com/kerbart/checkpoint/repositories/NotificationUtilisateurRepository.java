package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.UtilisateurNotification;

public interface NotificationUtilisateurRepository extends CrudRepository<UtilisateurNotification, Long> {
    
    @Query("SELECT n FROM UtilisateurNotification n WHERE n.utilisateur.token = :utilisateurToken AND n.cabinet.token = :cabinetToken AND n.type = NEW_COMMENT")
    List<UtilisateurNotification> findAllNewComments(@Param("utilisateurToken") String utilisateurToken, @Param("cabinetToken") String cabinetToken);
    
    @Query("SELECT n FROM UtilisateurNotification n WHERE n.utilisateur.token = :utilisateurToken AND n.cabinet.token = :cabinetToken AND n.type = NEW_PATIENT")
    List<UtilisateurNotification> findAllNewPatients(@Param("utilisateurToken") String utilisateurToken, @Param("cabinetToken") String cabinetToken);
    
    @Query("SELECT n FROM UtilisateurNotification n WHERE n.utilisateur.token = :utilisateurToken AND n.cabinet.token = :cabinetToken AND n.type = NEW_ORDONNANCE")
    List<UtilisateurNotification> findAllNewOrdonnances(@Param("utilisateurToken") String utilisateurToken, @Param("cabinetToken") String cabinetToken);
}
