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
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.model.UtilisateurNotification;
import com.kerbart.checkpoint.repositories.ApplicationRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;

@Service
public class NotificationService {


    @PersistenceContext
    private EntityManager em;

    @Inject
    ApplicationRepository applicationRepository;

    @Inject
    UtilisateurRepository utilisateurRepository;
	
    
    public List<Notification> getAllNotifications(Utilisateur utilisateur) {
    	 Query query = em.createQuery("select n from UtilisateurNotification n where n.utilisateur.token = :utilisateurToken ")
                 .setParameter("utilisateurToken", utilisateur.getToken());
    	 return (List<Notification>) query.getResultList();
    }
    
	public void notifyNewOrdonnanceCabinetUsers(Cabinet application, Utilisateur issuer, Ordonnance ordonnance) {
		for (Utilisateur u : findAllOtherUsers(application, issuer)) {
			UtilisateurNotification notification = new UtilisateurNotification();
			notification.setUtilisateur(u);
			notification.setNotification(NotificationType.NEW_ORDO);
			notification.setOrdonnance(ordonnance);
			em.persist(notification);
		}
	}

	public void notifyNewCommentCabinetUsers(Cabinet application, Utilisateur issuer, Commentaire commentaire) {
		for (Utilisateur u : findAllOtherUsers(application, issuer)) {
			UtilisateurNotification notification = new UtilisateurNotification();
			notification.setUtilisateur(u);
			notification.setNotification(NotificationType.NEW_COMMENT);
			notification.setCommentaire(commentaire);;
			em.persist(notification);
		}
	}

	private List<Utilisateur> findAllOtherUsers(Cabinet application, Utilisateur utilisateur) {
		List<Utilisateur> utilisateurs = utilisateurRepository.findByAppToken(application.getToken());
		utilisateurs.remove(utilisateur);
		return utilisateurs;
	}
	
}
