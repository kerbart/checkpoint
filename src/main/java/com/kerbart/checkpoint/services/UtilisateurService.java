package com.kerbart.checkpoint.services;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;

@Repository("utilisateurService")
@Transactional
public class UtilisateurService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    UtilisateurRepository utilisateurRepository;

    @Inject
    EncryptService encryptService;

    public Utilisateur create(String email, String password, String prenom, String nom)
            throws UserAlreadyExistsException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setPassword(encryptService.encryptPassword(password));
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        return create(utilisateur);
    }

    public Utilisateur create(Utilisateur utilisateur) throws UserAlreadyExistsException {
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
            throw new UserAlreadyExistsException();
        }
        em.persist(utilisateur);

        return utilisateur;
    }

    public Utilisateur update(Utilisateur utilisateur) {
        return em.merge(utilisateur);
    }

    public Utilisateur findUserByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Boolean auth(String email, String password) {
        Query query = em
                .createQuery(
                        "select u from Utilisateur u " + " where u.email = :email " + " and u.password = :password")
                .setParameter("email", email).setParameter("password", encryptService.encryptPassword(password));
        return query.getResultList() != null && query.getResultList().size() == 1;
    }

    public List<Utilisateur> listUsers() {
        Query query = em.createQuery("select u from Utilisateur u");
        return query.getResultList();
    }

}
