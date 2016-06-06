package com.kerbart.checkpoint.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.model.Utilisateur;

@Repository("utilisateurService")
@Transactional
public class UtilisateurService {

    @PersistenceContext
    private EntityManager em;

    public Utilisateur createUser(Utilisateur utilisateur) {
    	em.persist(utilisateur);	
    	return utilisateur;
    }
    
    public Utilisateur findUserByEmail(String email) {
    	 Query query = em.createQuery("select u from Utilisateur u where u.email = :email").setParameter("email", email);
         return (Utilisateur)query.getSingleResult();
    }
    
    public List<Utilisateur> listUsers() {
    	 Query query = em.createQuery("select u from Utilisateur u");
         return query.getResultList();
    }
 
    public void removeAllUsers() {
    	List<Utilisateur> utilisateurs = this.listUsers();
    	for (Utilisateur utilisateur : utilisateurs) {
    		em.remove(utilisateur);
    	}
    }
    
}
