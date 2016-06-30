package com.kerbart.checkpoint.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.kerbart.checkpoint.model.NotificationType;
import com.kerbart.checkpoint.model.UtilisateurNotification;

public interface NotificationUtilisateurRepository extends CrudRepository<UtilisateurNotification, Long> {
    
    @Query("SELECT n FROM UtilisateurNotification n WHERE n.utilisateur.token = :utilisateurToken AND n.cabinet.token = :cabinetToken AND n.type = :type")
    List<UtilisateurNotification> findAllNewByType(@Param("utilisateurToken") String utilisateurToken, @Param("cabinetToken") String cabinetToken, @Param("type") NotificationType type);
    
}
