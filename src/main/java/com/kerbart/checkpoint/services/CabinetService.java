package com.kerbart.checkpoint.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.exceptions.CabinetDoesNotExistException;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.model.Cabinet;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.model.UtilisateurCabinet;
import com.kerbart.checkpoint.repositories.CabinetRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;

@Repository("applicationService")
@Transactional
public class CabinetService {

	@PersistenceContext
	private EntityManager em;

	@Inject
	CabinetRepository applicationRepository;

	@Inject
	UtilisateurRepository utilisateurRepository;

	/**
	 * Create a new Application (main entry point of all application)
	 * 
	 * @param name
	 * @return
	 */
	public Cabinet createCabinet(String name) {
		Cabinet app = new Cabinet();
		app.setName(name);
		app.setCurrent(true);
		em.persist(app);
		return app;
	}

	public void resetAllCabinetToNotCurrent(String utilisateurToken) {
		List<Cabinet> apps = getApplicationByUtilisateur(utilisateurToken);
		for (Cabinet app : apps) {
			app.setCurrent(false);
			em.persist(app);
		}
	}

	public Cabinet getCurrentCabinet(Utilisateur utilisateur) {
		List<Cabinet> list = this.getApplicationByUtilisateur(utilisateur.getToken());
		Cabinet theCurrentApp = null;
		for (Cabinet app : list) {
			if (app.getCurrent()) {
				theCurrentApp = app;
			}
		}
		return theCurrentApp;
	}

	public Cabinet changeCurrentCabinet(Utilisateur utilisateur, String appToken) throws CabinetDoesNotExistException {
		resetAllCabinetToNotCurrent(utilisateur.getToken());
		Cabinet cabinet = applicationRepository.findByToken(appToken);
		if (cabinet == null) {
			throw new CabinetDoesNotExistException();
		}
		
		cabinet.setCurrent(true);
		em.persist(cabinet);
		return cabinet;
	}

	public void associateCabinetToUser(String shortCode, Utilisateur user)
			throws UserAlreadyAssociatedException, CabinetDoesNotExistException {
		Cabinet app = applicationRepository.findByShortCode(shortCode);
		if (app == null) {
			throw new CabinetDoesNotExistException();
		}
		this.associateCabinetToUser(app, user);
	}

	/**
	 * Link an application to a user
	 * 
	 * @param cabinet
	 * @param user
	 * @throws UserAlreadyAssociatedException
	 */
	public void associateCabinetToUser(Cabinet cabinet, Utilisateur user) throws UserAlreadyAssociatedException {
		List<Utilisateur> utilisateurs = this.getUtilisateursByApplication(cabinet.getToken());
		for (Utilisateur u : utilisateurs) {
			if (user.getId() == u.getId()) {
				throw new UserAlreadyAssociatedException("Utilisateur déjà associé à cette application");
			}
		}

		UtilisateurCabinet userApp = new UtilisateurCabinet();
		userApp.setCabinet(cabinet);
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
				.createQuery("select ua.utilisateur from UtilisateurCabinet ua where ua.cabinet.token = :token")
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
						"select ua.cabinet from UtilisateurCabinet ua  where ua.utilisateur.token = :token")
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
	 * @param cabinet
	 * @param utilisateur
	 * @return
	 */
	public Boolean checkApplicationBelongsUtilisateur(Cabinet cabinet, Utilisateur utilisateur) {
		Query query = em
				.createQuery("select ua from UtilisateurCabinet ua  "
						+ " where ua.utilisateur.token = :tokenUtilisateur "
						+ " and ua.cabinet.token = :tokenCabinet ")
				.setParameter("tokenUtilisateur", utilisateur.getToken())
				.setParameter("tokenCabinet", cabinet.getToken());
		List<UtilisateurCabinet> list = query.getResultList();
		if (list != null && list.size() == 1) {
			return true;
		}
		return false;
	}

}
