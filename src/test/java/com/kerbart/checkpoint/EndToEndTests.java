package com.kerbart.checkpoint;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.services.ApplicationService;
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
	
	@Test
	public void souldInsertTwoUsers() {
		utilisateurService.removeAllUsers();
		
		Utilisateur u1 = new Utilisateur();
		u1.setNom("KERBART");
		u1.setPrenom("Damien");
		u1.setEmail("damien@kerbart.com");
		u1.setPassword("123456789");
		
		utilisateurService.createUser(u1);
		
		assertEquals(1, utilisateurService.listUsers().size());

		Utilisateur u2 = new Utilisateur();
		u2.setNom("KERBART");
		u2.setPrenom("Damien");
		u2.setEmail("damien2@kerbart.com");
		u2.setPassword("123456789");
		
		utilisateurService.createUser(u2);

		assertEquals(2, utilisateurService.listUsers().size());
	}
	
	@Test(expected=DataIntegrityViolationException.class)
	public void shouldNotInsertTwoEmails()  {
		utilisateurService.removeAllUsers();
		
		Utilisateur u1 = new Utilisateur();
		u1.setNom("KERBART");
		u1.setPrenom("Damien");
		u1.setEmail("damien22@kerbart.com");
		u1.setPassword("123456789");
		
		utilisateurService.createUser(u1);
		

		Utilisateur u2 = new Utilisateur();
		u2.setNom("KERBART");
		u2.setPrenom("Damien");
		u2.setEmail("damien22@kerbart.com");
		u2.setPassword("123456789");
		
		utilisateurService.createUser(u2);

	}
	
	@Test
	public void userShouldBeLinkedToAnApplication() {
		utilisateurService.removeAllUsers();
		
		Utilisateur u1 = new Utilisateur();
		u1.setNom("KERBART");
		u1.setPrenom("Damien");
		u1.setEmail("damien22@kerbart.com");
		u1.setPassword("123456789");
		
		utilisateurService.createUser(u1);
		
		Application application = new Application();
		application.setName("Mon application");
		application.setToken("11234-44567-789846");
		application.setUtilisateurs(new HashSet<Utilisateur>());
		application.getUtilisateurs().add(u1);
		
		applicationService.createApp(application);
		u1 = utilisateurService.findUserByEmail("damien22@kerbart.com");
		
		assertEquals(1, u1.getApplications().size());
	}
	
	
}
