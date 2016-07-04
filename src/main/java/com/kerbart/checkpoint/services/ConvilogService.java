package com.kerbart.checkpoint.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConvilogService {

	public ResponseEntity<String> getListPatients(String login, String password) {
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());
		ResponseEntity<String> response = restTemplate
				.exchange("https://www.monconvilog.fr/v3.3.3/Authentication.svc/json/Login?_password=" + password
						+ "&_context=1&_login=" + login, HttpMethod.GET, entity, String.class);

		String sessionCookie = extractSessionCookie(response);
		System.out.println("Found sessionCookie : " + sessionCookie);

		// auth
		RestTemplate restTemplate2 = new RestTemplate();
		HttpHeaders headers =  new HttpHeaders();
		headers.add("Cookie", ".AspNet.ApplicationCookie=" +  sessionCookie);
		HttpEntity<?> entity2 = new HttpEntity<>(headers);
		ResponseEntity<String> response2 = restTemplate
				.exchange("https://www.monconvilog.fr/1.02.06/v3.3.3/Datas.svc/json/GetCustomers?_phone=&_name=&_address=&_firstName=&_city=&_withMutuelles=true&_withNBT=true",
						HttpMethod.GET, entity2, String.class);
		
		return response2;
	}

	private String extractSessionCookie(ResponseEntity<String> response) {
		String cookie = "";
		for (String key : response.getHeaders().keySet()) {
			for (String line : response.getHeaders().get(key)) {
				if (line.contains(".AspNet.ApplicationCookie")) {
					cookie = line.replaceAll(".AspNet.ApplicationCookie=", "");
				}
			}
		}
		return cookie;
	}

	//
	// private HashMap<String, String> login(String email, String password) {
	// String url =
	// "https://www.monconvilog.fr/1.02.06/v3.3.3/Datas.svc/json/GetCustomers?_phone=&_name=&_address=&_firstName=&_city=&_withMutuelles=true&_withNBT=true";
	// }

}
