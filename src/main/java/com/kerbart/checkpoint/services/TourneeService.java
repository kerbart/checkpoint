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

import com.kerbart.checkpoint.model.Cabinet;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.PatientDansTournee;
import com.kerbart.checkpoint.model.Tournee;
import com.kerbart.checkpoint.model.TourneeOccurence;
import com.kerbart.checkpoint.repositories.TourneeOccurenceRepository;
import com.kerbart.checkpoint.repositories.TourneeRepository;

@Repository("tourneeService")
@Transactional
public class TourneeService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    TourneeRepository tourneeRepository;

    @Inject
    TourneeOccurenceRepository tourneeOccurenceRepository;

    public Tournee createTournee(Cabinet cabinet, String name) {
        Tournee tournee = new Tournee(cabinet, name);
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

    public PatientDansTournee addPatientToTourneeOccurence(TourneeOccurence tourneeOccurence, Patient patient,
            Integer ordre) {
        PatientDansTournee pdt = new PatientDansTournee();
        pdt.setOrdre(ordre);
        pdt.setPatient(patient);
        return addPatientToTourneeOccurence(tourneeOccurence, pdt);
    }

    public TourneeOccurence findTourneeOccurenceByToken(String token) {
        return tourneeOccurenceRepository.findByToken(token);
    }

    public Tournee findTourneeByToken(String token) {
        return tourneeRepository.findByToken(token);
    }

    public PatientDansTournee addPatientToTourneeOccurence(TourneeOccurence tourneeOccurence, PatientDansTournee pdt) {
        pdt.setTourneeOccurence(tourneeOccurence);
        em.persist(pdt);
        return pdt;
    }

    public List<Tournee> listTourneeByCabinet(String appToken) {
        Query query = em.createQuery("select t from Tournee t  where t.cabinet.token = :token")
                .setParameter("token", appToken);
        return (List<Tournee>) query.getResultList();
    }

    public List<TourneeOccurence> listTourneeOccurenceByTournee(String tourneeToken) {
        Query query = em.createQuery("select to from TourneeOccurence to  where to.tournee.token = :token")
                .setParameter("token", tourneeToken);
        return (List<TourneeOccurence>) query.getResultList();
    }

    public TourneeOccurence duplicateTourneeOccurence(TourneeOccurence tourneeOccurence, Date newDate) {
        TourneeOccurence tourneeOccurenceNew = new TourneeOccurence();
        tourneeOccurenceNew.setTournee(tourneeOccurence.getTournee());
        tourneeOccurenceNew.setDateDebut(newDate);
        tourneeOccurenceNew.setPatients(new HashSet<PatientDansTournee>());
        em.persist(tourneeOccurenceNew);
        for (PatientDansTournee pdt : tourneeOccurence.getPatients()) {
            PatientDansTournee pdtClone = new PatientDansTournee();

            pdtClone.setPatient(pdt.getPatient());
            pdtClone.setOrdre(pdt.getOrdre());
            pdtClone.setTarif(pdt.getTarif());
            em.persist(pdtClone);
            addPatientToTourneeOccurence(tourneeOccurenceNew, pdtClone);
        }

        return tourneeOccurenceNew;
    }

}
