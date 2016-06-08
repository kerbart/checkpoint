package com.kerbart.checkpoint.services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.PatientDansTournee;
import com.kerbart.checkpoint.model.Tournee;
import com.kerbart.checkpoint.model.TourneeOccurence;
import com.kerbart.checkpoint.repositories.TourneeRepository;

@Repository("tourneeService")
@Transactional
public class TourneeService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    TourneeRepository tourneeRepository;

    public Tournee createTournee(Application application, String name) {
        Tournee tournee = new Tournee();
        tournee.setApplication(application);
        tournee.setDateCreation(new Date());
        tournee.setName(name);
        em.persist(tournee);
        return tournee;
    }

    public TourneeOccurence createTourneeOccurence(Tournee tournee, Date dateDebut) {
        TourneeOccurence tourneeOccurence = new TourneeOccurence();
        tourneeOccurence.setDateDebut(dateDebut);
        tourneeOccurence.setTournee(tournee);
        em.persist(tourneeOccurence);
        return tourneeOccurence;
    }

    public PatientDansTournee addToTourneeOccurence(TourneeOccurence to, PatientDansTournee pdt) {
        to.getPatients().add(pdt);
        em.persist(to);
        return pdt;
    }

    public TourneeOccurence duplicateTourneeOccurence(TourneeOccurence tourneeOccurence, Date newDate) {
        TourneeOccurence tourneeOccurenceNew = new TourneeOccurence();
        tourneeOccurenceNew.setTournee(tourneeOccurence.getTournee());
        tourneeOccurenceNew.setDateDebut(newDate);
        tourneeOccurenceNew.setPatients(new HashSet<PatientDansTournee>());
        em.persist(tourneeOccurenceNew);
        for (PatientDansTournee pdt : tourneeOccurence.getPatients()) {
            PatientDansTournee pdtClone = new PatientDansTournee();
            pdtClone.setPersonne(pdt.getPersonne());
            pdtClone.setOrdre(pdt.getOrdre());
            pdtClone.setTarif(pdt.getTarif());
            em.persist(pdtClone);
            addToTourneeOccurence(tourneeOccurenceNew, pdtClone);
        }

        return tourneeOccurenceNew;
    }

    public void wipeAll() {
        Query query = em.createQuery("select to from TourneeOccurence to ");
        for (TourneeOccurence to : (List<TourneeOccurence>) query.getResultList()) {
            em.remove(to);
        }
        Query query2 = em.createQuery("select t from Tournee t ");
        for (Tournee t : (List<Tournee>) query2.getResultList()) {
            em.remove(t);
        }
    }

}
