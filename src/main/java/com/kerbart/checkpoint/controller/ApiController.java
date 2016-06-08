package com.kerbart.checkpoint.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kerbart.checkpoint.controller.dto.UserLoginDTO;
import com.kerbart.checkpoint.controller.responses.ErrorCode;
import com.kerbart.checkpoint.controller.responses.UtilisateurResponse;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;
import com.kerbart.checkpoint.services.ApplicationService;
import com.kerbart.checkpoint.services.UtilisateurService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/")
@Api(value = "Checkpoint API")
public class ApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Inject
    UtilisateurService utilisateurService;

    @Inject
    UtilisateurRepository utilisateurRepository;

    @Inject
    ApplicationService applicationService;

    @ApiOperation(value = "User login")
    @RequestMapping(value = "/user/login", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<UtilisateurResponse> login(@RequestBody UserLoginDTO credentials) {
        UtilisateurResponse response = new UtilisateurResponse();
        if (utilisateurService.auth(credentials.getEmail(), credentials.getPassword())) {
            Utilisateur utilisateur = utilisateurService.findUserByEmail(credentials.getEmail());
            response.setUtilisateur(utilisateur);
            response.setError(null);
            return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);
        } else {
            response.setError(ErrorCode.USER_BAD_CREDENTIALS);
            return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "User sign in")
    @RequestMapping(value = "/user/signin", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<UtilisateurResponse> signIn(@RequestBody UserLoginDTO credentials) {
        UtilisateurResponse response = new UtilisateurResponse();
        Utilisateur utilisateur;
        try {
            utilisateur = utilisateurService.create(credentials.getEmail(), credentials.getPassword(), "", "");
            response.setError(null);
            response.setUtilisateur(utilisateur);
            return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);
        } catch (UserAlreadyExistsException e) {
            response.setError(ErrorCode.USER_ALREADY_EXISTS);
        }
        return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);

    }

    @ApiOperation(value = "App list")
    @RequestMapping(value = "/user/{token}/app/list", produces = "application/json", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Application>> listMyApplication(@PathVariable("token") String token) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(token);
        if (utilisateur != null) {
            List<Application> list = applicationService.getApplicationByUtilisateur(utilisateur.getToken());
            return new ResponseEntity<List<Application>>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<List<Application>>(new ArrayList<Application>(), HttpStatus.OK);
        }
    }

    @ApiOperation(value = "App creation")
    @RequestMapping(value = "/user/{token}/app/create", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Application>> createNewApplication(@PathVariable("token") String token,
            @RequestBody String appName) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(token);
        if (utilisateur != null) {
            Application application = applicationService.createApp(appName);
            try {
                applicationService.associateApplicationToUser(application, utilisateur);
            } catch (UserAlreadyAssociatedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return listMyApplication(token);
    }

}
