package com.kerbart.checkpoint.services;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.kerbart.checkpoint.spring.AppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/application-context-test.xml")
@SpringApplicationConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class ConvilogServiceTest {

	@Inject
	ConvilogService service;
	
	@Test
	public void shouldConnectToConvilog() {
		ResponseEntity<String> response = service.getListPatients("******************", "************");
		System.out.println(response.getBody());
		for (String key : response.getHeaders().keySet()) {
			System.out.println(key + " // " + response.getHeaders().get(key));
		}
	}
	
}
