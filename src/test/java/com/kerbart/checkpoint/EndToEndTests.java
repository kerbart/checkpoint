package com.kerbart.checkpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kerbart.checkpoint.exceptions.ApplicationDoesNotExistException;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Ordonnance;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.Tournee;
import com.kerbart.checkpoint.model.TourneeOccurence;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.repositories.OrdonnanceRepository;
import com.kerbart.checkpoint.repositories.PatientRepository;
import com.kerbart.checkpoint.services.ApplicationService;
import com.kerbart.checkpoint.services.PatientService;
import com.kerbart.checkpoint.services.TourneeService;
import com.kerbart.checkpoint.services.UtilisateurService;
import com.kerbart.checkpoint.spring.AppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/application-context-test.xml")
@SpringApplicationConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class EndToEndTests {

    @Inject
    UtilisateurService utilisateurService;

    @Inject
    ApplicationService applicationService;

    @Inject
    TourneeService tourneeService;

    @Inject
    PatientService patientService;

    @Inject
    PatientRepository patientRepository;

    @Inject
    OrdonnanceRepository ordonnanceRepository;

    private void wipeAll() {

        // tourneeService.wipeAll();
        // applicationService.wipeAll();
        // utilisateurService.removeAllUsers();
    }

    @Test
    @DirtiesContext
    public void souldInsertTwoUsers() throws UserAlreadyExistsException {
        this.wipeAll();

        utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");

        assertEquals(1, utilisateurService.listUsers().size());

        utilisateurService.create("damien2@kerbart.com", "toto1234", "Damien", "Kerbart");

        assertEquals(2, utilisateurService.listUsers().size());
    }

    @Test(expected = UserAlreadyExistsException.class)
    @DirtiesContext
    public void shouldNotInsertTwoEmails() throws UserAlreadyExistsException {
        this.wipeAll();

        Utilisateur u1 = new Utilisateur();
        u1.setNom("KERBART");
        u1.setPrenom("Damien");
        u1.setEmail("damien22@kerbart.com");
        u1.setPassword("123456789");

        utilisateurService.create(u1);

        Utilisateur u2 = new Utilisateur();
        u2.setNom("KERBART");
        u2.setPrenom("Damien");
        u2.setEmail("damien22@kerbart.com");
        u2.setPassword("123456789");

        utilisateurService.create(u2);

    }

    @Test
    @DirtiesContext
    public void userShouldBeLinkedToAnApplication() throws UserAlreadyAssociatedException, UserAlreadyExistsException {
        this.wipeAll();

        Utilisateur u1 = new Utilisateur();
        u1.setNom("KERBART");
        u1.setPrenom("Damien");
        u1.setEmail("damien22@kerbart.com");
        u1.setPassword("123456789");

        u1 = utilisateurService.create(u1);
        assertNotEquals(null, u1.getToken());

        Utilisateur u2 = new Utilisateur();
        u2.setNom("KERBART");
        u2.setPrenom("Damien");
        u2.setEmail("damien20@kerbart.com");
        u2.setPassword("123456789");

        u2 = utilisateurService.create(u2);
        assertNotEquals(null, u2.getToken());

        Application application = applicationService.createApp("An Application");
        applicationService.associateApplicationToUser(application, u1);
        List<Utilisateur> users = applicationService.getUtilisateursByApplication(application.getToken());
        assertEquals(1, users.size());

        applicationService.associateApplicationToUser(application, u2);
        users = applicationService.getUtilisateursByApplication(application.getToken());
        assertEquals(2, users.size());

    }

    @Test(expected = UserAlreadyAssociatedException.class)
    @DirtiesContext
    public void cannotAdd2timesSameUserToApplicationUtilisateur() throws Throwable {
        this.wipeAll();

        Utilisateur u1 = new Utilisateur();
        u1.setNom("KERBART");
        u1.setPrenom("Damien");
        u1.setEmail("damien22@kerbart.com");
        u1.setPassword("123456789");

        utilisateurService.create(u1);
        Application application = applicationService.createApp("An Application");
        applicationService.associateApplicationToUser(application, u1);
        applicationService.associateApplicationToUser(application, u1);
    }

    @Test
    @DirtiesContext
    public void shouldAuthUser() throws Throwable {
        this.wipeAll();
        utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        assertEquals(true, utilisateurService.auth("damien@kerbart.com", "toto1234"));
        assertEquals(false, utilisateurService.auth("damien@kerbart.com", "anOtherPassword"));
    }

    @Test
    @DirtiesContext
    public void shouldCreateUserAppAndTournee() throws UserAlreadyAssociatedException, UserAlreadyExistsException {
        this.wipeAll();
        Utilisateur u = utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        Application app = applicationService.createApp("My App");
        applicationService.associateApplicationToUser(app, u);
        Tournee tournee = tourneeService.createTournee(app, "Tournée 1");
        TourneeOccurence tourneeOccurence = tourneeService.createTourneeOccurence(tournee, new Date());
        assertNotEquals(null, tourneeOccurence.getTournee());
        assertEquals("My App", tourneeOccurence.getTournee().getApplication().getName());
    }

    @Test
    @DirtiesContext
    public void shouldCheckIfApplicationBelongsToUtilisateur()
            throws UserAlreadyAssociatedException, UserAlreadyExistsException {
        this.wipeAll();
        Utilisateur u = utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        Application app = applicationService.createApp("My App");
        Application app2 = applicationService.createApp("My App 2");
        applicationService.associateApplicationToUser(app, u);
        assertEquals(true, applicationService.checkApplicationBelongsUtilisateur(app, u));
        assertEquals(false, applicationService.checkApplicationBelongsUtilisateur(app2, u));
    }

    @Test
    @DirtiesContext
    public void shouldCreateUserApplicationTourneeOrrurenceAndPatients()
            throws UserAlreadyAssociatedException, UserAlreadyExistsException, ApplicationDoesNotExistException {
        Utilisateur u = utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        Application app = applicationService.createApp("My App");
        applicationService.associateApplicationToUser(app, u);
        Tournee tournee = tourneeService.createTournee(app, "Ma Tournee");
        TourneeOccurence tourneeOccurence = tourneeService.createTourneeOccurence(tournee, new Date());
        Patient p1 = patientService.createPatient(createRandomPatient(), app.getToken());
        Patient p2 = patientService.createPatient(createRandomPatient(), app.getToken());
        Patient p3 = patientService.createPatient(createRandomPatient(), app.getToken());
        tourneeService.addPatientToTourneeOccurence(tourneeOccurence, p1, 1);
        tourneeService.addPatientToTourneeOccurence(tourneeOccurence, p2, 2);
        tourneeService.addPatientToTourneeOccurence(tourneeOccurence, p3, 3);

        TourneeOccurence tourneeOccurence2 = tourneeService.duplicateTourneeOccurence(
                tourneeService.findTourneeOccurenceByToken(tourneeOccurence.getToken()), new Date());

        System.out.println("Utilisateur " + u.getNom() + " / " + u.getToken());
        for (Application app2 : applicationService.getApplicationByUtilisateur(u.getToken())) {
            System.out.println("+-> found app = " + app.getName() + " / " + app.getToken());
            for (Tournee t : tourneeService.listTourneeByApplication(app2.getToken())) {
                System.out.println("  +-> found tournee " + t.getName() + " / " + t.getToken());
                for (TourneeOccurence to : tourneeService.listTourneeOccurenceByTournee(t.getToken())) {
                    System.out.println("      +-> found tournee occurence " + to.getPatients().size() + " patients ");
                }
            }
        }

        patientService.removePatientDansTourneeOccurence(p3.getToken(), tourneeOccurence2.getToken());

        System.out.println("Utilisateur " + u.getNom() + " / " + u.getToken());
        for (Application app2 : applicationService.getApplicationByUtilisateur(u.getToken())) {
            System.out.println("+-> found app = " + app.getName() + " / " + app.getToken());
            for (Tournee t : tourneeService.listTourneeByApplication(app2.getToken())) {
                System.out.println("  +-> found tournee " + t.getName() + " / " + t.getToken());
                for (TourneeOccurence to : tourneeService.listTourneeOccurenceByTournee(t.getToken())) {
                    System.out.println("      +-> found tournee occurence " + to.getPatients().size() + " patients ");
                }
            }
        }
    }

    @Test
    @DirtiesContext
    public void shouldCreateUserApplicationPatientAndUpdateIt()
            throws UserAlreadyAssociatedException, UserAlreadyExistsException, ApplicationDoesNotExistException {
        Utilisateur u = utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        Application app = applicationService.createApp("My App");
        applicationService.associateApplicationToUser(app, u);
        Tournee tournee = tourneeService.createTournee(app, "Ma Tournee");
        TourneeOccurence tourneeOccurence = tourneeService.createTourneeOccurence(tournee, new Date());
        Patient patient = patientService.createPatient(createRandomPatient(), app.getToken());

        patient.setNom("BIDULE");
        patientService.updatePatient(patient, app.getToken());

        Patient retrievedPatient = patientRepository.findByToken(patient.getToken());

        assertEquals("BIDULE", retrievedPatient.getNom());

    }

    @Test
    @DirtiesContext
    public void shouldCreateUserApplicationPatientAndOrdonance()
            throws UserAlreadyAssociatedException, UserAlreadyExistsException, ApplicationDoesNotExistException {
        Utilisateur u = utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        Application app = applicationService.createApp("My App");
        applicationService.associateApplicationToUser(app, u);
        Tournee tournee = tourneeService.createTournee(app, "Ma Tournee");
        TourneeOccurence tourneeOccurence = tourneeService.createTourneeOccurence(tournee, new Date());
        Patient patient = patientService.createPatient(createRandomPatient(), app.getToken());
        Ordonnance ordonnance = patientService.createOrdonance(patient, app.getToken(), new Date(), new Date(), "");
        patientService.addFileOrdonance(app.getToken(), ordonnance.getToken(), "image/png", "Du contenu".getBytes());
        ordonnance = ordonnanceRepository.findByToken(ordonnance.getToken());
        assertEquals(1, ordonnance.getFiles().size());

    }

    public byte[] extractBytes(String ImageName) throws IOException {
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = ImageIO.read(imgPath);

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage.getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        return (data.getData());
    }

    private Patient createRandomPatient() {
        Patient p = new Patient();
        p.setNom(RandomStringUtils.randomAlphanumeric(18));
        p.setPrenom(RandomStringUtils.randomAlphanumeric(18));
        p.setNumeroSS("0123456789456123");
        return p;
    }

}
