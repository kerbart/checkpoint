package com.kerbart.checkpoint.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.helper.TokenHelper;
import com.kerbart.checkpoint.model.Application;
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
    public Application createApp(String name) {
        Application app = new Application();
        app.setName(name);
        app.setToken(TokenHelper.generateToken());
        em.persist(app);
        return app;
    }

    /**
     * Link an application to a user
     * 
     * @param app
     * @param user
     * @throws UserAlreadyAssociatedException
     */
    public void associateApplicationToUser(Application app, Utilisateur user) throws UserAlreadyAssociatedException {
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
                .createQuery("select ua.utilisateur from UtilisateurApplication uawhere ua.application.token = :token")
                .setParameter("token", applicationToken);
        return (List<Utilisateur>) query.getResultList();
    }

    /**
     * List all application for a user
     * 
     * @param utilisateurToken
     * @return
     */
    public List<Application> getApplicationByUtilisateur(String utilisateurToken) {
        Query query = em
                .createQuery(
                        "select ua.application from UtilisateurApplication ua  where ua.utilisateur.token = :token")
                .setParameter("token", utilisateurToken);
        return (List<Application>) query.getResultList();
    }

    /**
     * Check if an application belongs to a user (with tokens)
     * 
     * @param appToken
     * @param utilisateurToken
     * @return
     */
    public Boolean checkApplicationBelongsUtilisateur(String appToken, String utilisateurToken) {
        Application app = applicationRepository.findByToken(appToken);
        Utilisateur utilisateur = utilisateurRepository.findByToken(utilisateurToken);
        return checkApplicationBelongsUtilisateur(app, utilisateur);
    }

    /**
     * Check if an application belongs to a user (with pojos)
     * 
     * @param application
     * @param utilisateur
     * @return
     */
    public Boolean checkApplicationBelongsUtilisateur(Application application, Utilisateur utilisateur) {
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

    public void wipeAll() {
        Query query = em.createQuery("select ua " + " from UtilisateurApplication ua ");
        for (UtilisateurApplication ua : (List<UtilisateurApplication>) query.getResultList()) {
            em.remove(ua);
        }

        Query query2 = em.createQuery("select a " + " from Application a ");
        for (Application a : (List<Application>) query2.getResultList()) {
            em.remove(a);
        }
    }

}
