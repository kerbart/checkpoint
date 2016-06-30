package com.kerbart.checkpoint.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.management.Notification;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.kerbart.checkpoint.model.Cabinet;
import com.kerbart.checkpoint.model.Commentaire;
import com.kerbart.checkpoint.model.NotificationType;
import com.kerbart.checkpoint.model.Ordonnance;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.model.UtilisateurNotification;
import com.kerbart.checkpoint.repositories.CabinetRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;

@Service
public class NotificationService {


    @PersistenceContext
    private EntityManager em;

    @Inject
    CabinetRepository applicationRepository;

    @Inject
    UtilisateurRepository utilisateurRepository;
	
    
   
    
	public void notifyNewOrdonnanceCabinetUsers(Cabinet cabinet, Utilisateur issuer, Ordonnance ordonnance) {
		for (Utilisateur u : findAllOtherUsers(cabinet, issuer)) {
			UtilisateurNotification notification = new UtilisateurNotification();
			notification.setUtilisateur(u);
			notification.setCabinet(cabinet);
			notification.setType(NotificationType.NEW_ORDONNANCE);
			notification.setOrdonnance(ordonnance);
			em.persist(notification);
		}
	}

	public void notifyNewCommentCabinetUsers(Cabinet cabinet, Utilisateur issuer, Commentaire commentaire) {
		for (Utilisateur u : findAllOtherUsers(cabinet, issuer)) {
			UtilisateurNotification notification = new UtilisateurNotification();
			notification.setUtilisateur(u);
			notification.setCabinet(cabinet);
			notification.setType(NotificationType.NEW_COMMENT);
			notification.setCommentaire(commentaire);;
			em.persist(notification);
		}
	}
	

	public void notifyNewPatientCabinetUsers(Cabinet cabinet, Utilisateur issuer, Patient patient) {
		for (Utilisateur u : findAllOtherUsers(cabinet, issuer)) {
			UtilisateurNotification notification = new UtilisateurNotification();
			notification.setUtilisateur(u);
			notification.setCabinet(cabinet);
			notification.setType(NotificationType.NEW_PATIENT);
			notification.setPatient(patient);;
			em.persist(notification);
		}
	}
	
	private List<Utilisateur> findAllOtherUsers(Cabinet application, Utilisateur utilisateur) {
		List<Utilisateur> utilisateurs = utilisateurRepository.findByAppToken(application.getToken());
		utilisateurs.remove(utilisateur);
		return utilisateurs;
	}
	
}
