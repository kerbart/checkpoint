package com.kerbart.checkpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Tournee;
import com.kerbart.checkpoint.model.TourneeOccurence;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.services.ApplicationService;
import com.kerbart.checkpoint.services.TourneeService;
import com.kerbart.checkpoint.services.UtilisateurService;
import com.kerbart.checkpoint.spring.AppConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/application-context-test.xml")
@SpringApplicationConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
@EnableSwagger2
public class EndToEndTests {

    @Inject
    UtilisateurService utilisateurService;

    @Inject
    ApplicationService applicationService;

    @Inject
    TourneeService tourneeService;

    private void wipeAll() {
        tourneeService.wipeAll();
        applicationService.wipeAll();
        utilisateurService.removeAllUsers();
    }

    @Test
    public void souldInsertTwoUsers() throws UserAlreadyExistsException {
        this.wipeAll();

        utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");

        assertEquals(1, utilisateurService.listUsers().size());

        utilisateurService.create("damien2@kerbart.com", "toto1234", "Damien", "Kerbart");

        assertEquals(2, utilisateurService.listUsers().size());
    }

    @Test(expected = UserAlreadyExistsException.class)
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
    public void userShouldBeLinkedToAnApplication() throws UserAlreadyAssociatedException, UserAlreadyExistsException {
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
        u2.setEmail("damien20@kerbart.com");
        u2.setPassword("123456789");

        utilisateurService.create(u2);

        Application application = applicationService.createApp("An Application");
        applicationService.associateApplicationToUser(application, u1);
        List<Utilisateur> users = applicationService.getUtilisateursByApplication(application.getToken());
        assertEquals(1, users.size());

        applicationService.associateApplicationToUser(application, u2);
        users = applicationService.getUtilisateursByApplication(application.getToken());
        assertEquals(2, users.size());

    }

    @Test(expected = UserAlreadyAssociatedException.class)
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
    public void shouldAuthUser() throws Throwable {
        this.wipeAll();
        utilisateurService.create("damien@kerbart.com", "toto1234", "Damien", "Kerbart");
        assertEquals(true, utilisateurService.auth("damien@kerbart.com", "toto1234"));
        assertEquals(false, utilisateurService.auth("damien@kerbart.com", "anOtherPassword"));
    }

    @Test
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

}
