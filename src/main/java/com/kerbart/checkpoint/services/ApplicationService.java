package com.kerbart.checkpoint.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.exceptions.ApplicationDoesNotExistException;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.model.Cabinet;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.model.UtilisateurApplication;
import com.kerbart.checkpoint.repositories.ApplicationRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;

@Repository("applicationService")
@Transactional
public class ApplicationService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	ApplicationRepository applicationRepository;

	@Inject
	UtilisateurRepository utilisateurRepository;

	/**
	 * Create a new Application (main entry point of all application)
	 * 
	 * @param name
	 * @return
	 */
	public Cabinet createApp(String name) {
		Cabinet app = new Cabinet();
		app.setName(name);
		app.setCurrent(true);
		em.persist(app);
		return app;
	}

	public void resetAllAppToNotCurrent(String utilisateurToken) {
		List<Cabinet> apps = getApplicationByUtilisateur(utilisateurToken);
		for (Cabinet app : apps) {
			app.setCurrent(false);
			em.persist(app);
		}
	}

	public Cabinet getCurrentApp(Utilisateur utilisateur) {
		List<Cabinet> list = this.getApplicationByUtilisateur(utilisateur.getToken());
		Cabinet theCurrentApp = null;
		for (Cabinet app : list) {
			if (app.getCurrent()) {
				theCurrentApp = app;
			}
		}
		return theCurrentApp;
	}

	public Cabinet changeCurrentApp(Utilisateur utilisateur, String appToken) throws ApplicationDoesNotExistException {
		resetAllAppToNotCurrent(utilisateur.getToken());
		Cabinet cabinet = applicationRepository.findByToken(appToken);
		if (cabinet == null) {
			throw new ApplicationDoesNotExistException();
		}
		
		cabinet.setCurrent(true);
		em.persist(cabinet);
		return cabinet;
	}

	public void associateApplicationToUser(String shortCode, Utilisateur user)
			throws UserAlreadyAssociatedException, ApplicationDoesNotExistException {
		Cabinet app = applicationRepository.findByShortCode(shortCode);
		if (app == null) {
			throw new ApplicationDoesNotExistException();
		}
		this.associateApplicationToUser(app, user);
	}

	/**
	 * Link an application to a user
	 * 
	 * @param app
	 * @param user
	 * @throws UserAlreadyAssociatedException
	 */
	public void associateApplicationToUser(Cabinet app, Utilisateur user) throws UserAlreadyAssociatedException {
		List<Utilisateur> utilisateurs = this.getUtilisateursByApplication(app.getToken());
		for (Utilisateur u : utilisateurs) {
			if (user.getId() == u.getId()) {
				throw new UserAlreadyAssociatedException("Utilisateur déjà associé à cette application");
			}
		}

		UtilisateurApplication userApp = new UtilisateurApplication();
		userApp.setApplication(app);
		userApp.setUtilisateur(user);
		em.persist(userApp);
	}

	/**
	 * List all users of an application
	 * 
	 * @param applicationToken
	 * @return
	 */
	public List<Utilisateur> getUtilisateursByApplication(String applicationToken) {
		Query query = em
				.createQuery("select ua.utilisateur from UtilisateurApplication ua where ua.application.token = :token")
				.setParameter("token", applicationToken);
		return (List<Utilisateur>) query.getResultList();
	}

	/**
	 * List all application for a user
	 * 
	 * @param utilisateurToken
	 * @return
	 */
	public List<Cabinet> getApplicationByUtilisateur(String utilisateurToken) {
		Query query = em
				.createQuery(
						"select ua.application from UtilisateurApplication ua  where ua.utilisateur.token = :token")
				.setParameter("token", utilisateurToken);
		return (List<Cabinet>) query.getResultList();
	}

	/**
	 * Check if an application belongs to a user (with tokens)
	 * 
	 * @param appToken
	 * @param utilisateurToken
	 * @return
	 */
	public Boolean checkApplicationBelongsUtilisateur(String appToken, String utilisateurToken) {
		Cabinet app = applicationRepository.findByToken(appToken);
		Utilisateur utilisateur = utilisateurRepository.findByToken(utilisateurToken);
		if (app == null || utilisateur == null) {
			return false;
		}

		return checkApplicationBelongsUtilisateur(app, utilisateur);
	}

	/**
	 * Check if an application belongs to a user (with pojos)
	 * 
	 * @param application
	 * @param utilisateur
	 * @return
	 */
	public Boolean checkApplicationBelongsUtilisateur(Cabinet application, Utilisateur utilisateur) {
		Query query = em
				.createQuery("select ua from UtilisateurApplication ua  "
						+ " where ua.utilisateur.token = :tokenUtilisateur "
						+ " and ua.application.token = :tokenApplication ")
				.setParameter("tokenUtilisateur", utilisateur.getToken())
				.setParameter("tokenApplication", application.getToken());
		List<UtilisateurApplication> list = query.getResultList();
		if (list != null && list.size() == 1) {
			return true;
		}
		return false;
	}

}
