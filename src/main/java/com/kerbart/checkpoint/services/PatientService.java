package com.kerbart.checkpoint.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.kerbart.checkpoint.exceptions.ApplicationDoesNotExistException;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.PatientDansTournee;
import com.kerbart.checkpoint.repositories.ApplicationRepository;

@Repository("patientService")
@Transactional
public class PatientService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    ApplicationRepository applicationRepository;

    public Patient createPatient(Patient patient, String applicationToken) throws ApplicationDoesNotExistException {
        Application app = applicationRepository.findByToken(applicationToken);
        if (app == null) {
            throw new ApplicationDoesNotExistException();
        }
        patient.setApplication(app);
        em.persist(patient);
        return patient;
    }

    public Patient updatePatient(Patient patient, String applicationToken) throws ApplicationDoesNotExistException {
        Application app = applicationRepository.findByToken(applicationToken);
        if (app == null) {
            throw new ApplicationDoesNotExistException();
        }
        patient.setApplication(app);
        return em.merge(patient);
    }

    public PatientDansTournee findPatientDansTourneeOccurence(String patientToken, String tourneeOccurenceToken) {
        Query query = em
                .createQuery("select pdt from PatientDansTournee pdt "
                        + " where pdt.tourneeOccurence.token = :tourneeOccurenceToken "
                        + " and pdt.patient.token = :patientToken")
                .setParameter("tourneeOccurenceToken", tourneeOccurenceToken)
                .setParameter("patientToken", patientToken);
        return (PatientDansTournee) query.getSingleResult();
    }

    public void removePatientDansTourneeOccurence(String patientToken, String tourneeOccurenceToken) {
        PatientDansTournee pdt = this.findPatientDansTourneeOccurence(patientToken, tourneeOccurenceToken);
        if (pdt != null) {
            em.remove(pdt);
        }
    }

}
