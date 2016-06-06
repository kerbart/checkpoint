package com.kerbart.checkpoint.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/")
@Api(value = "Checkpoint API")
public class ApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);
	
	private final static String TOKEN = "YIN5-B4UN-YL28-RSEI";
	
	@ApiOperation(value = "URL de test")
	@RequestMapping(value = "/test", produces = "application/json", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public ResponseEntity<String> test() {
		return new ResponseEntity<String>("", HttpStatus.ACCEPTED);
	}
	
}
