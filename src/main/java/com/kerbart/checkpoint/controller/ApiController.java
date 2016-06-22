package com.kerbart.checkpoint.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kerbart.checkpoint.controller.dto.OrdonnanceDTO;
import com.kerbart.checkpoint.controller.dto.PatientDTO;
import com.kerbart.checkpoint.controller.dto.PatientSearchDTO;
import com.kerbart.checkpoint.controller.dto.UserLoginDTO;
import com.kerbart.checkpoint.controller.responses.ErrorCode;
import com.kerbart.checkpoint.controller.responses.FileResponse;
import com.kerbart.checkpoint.controller.responses.OrdonnanceResponse;
import com.kerbart.checkpoint.controller.responses.PatientResponse;
import com.kerbart.checkpoint.controller.responses.PatientsResponse;
import com.kerbart.checkpoint.controller.responses.UtilisateurResponse;
import com.kerbart.checkpoint.exceptions.ApplicationDoesNotExistException;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.model.Application;
import com.kerbart.checkpoint.model.Ordonnance;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.SecuredFile;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.repositories.PatientRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;
import com.kerbart.checkpoint.services.ApplicationService;
import com.kerbart.checkpoint.services.PatientService;
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

    @Inject
    PatientService patientService;

    @Inject
    PatientRepository patientRepository;

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
    public ResponseEntity<Application> createNewApplication(@PathVariable("token") String token,
            @RequestBody String appName) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(token);
        Application application = null;
        if (utilisateur != null) {
            application = applicationService.createApp(appName);
            try {
                applicationService.associateApplicationToUser(application, utilisateur);
            } catch (UserAlreadyAssociatedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new ResponseEntity<Application>(application, HttpStatus.OK);
    }

    @ApiOperation(value = "Patient creation")
    @RequestMapping(value = "/patient/create", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<PatientResponse> createNewPatient(@RequestBody PatientDTO patientDto) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(patientDto.getUtilisateurToken());
        if (utilisateur == null) {
            return new ResponseEntity<PatientResponse>(new PatientResponse(ErrorCode.USER_UNKONWN), HttpStatus.OK);
        }
        if (!applicationService.checkApplicationBelongsUtilisateur(patientDto.getApplicationToken(),
                patientDto.getUtilisateurToken())) {
            return new ResponseEntity<PatientResponse>(
                    new PatientResponse(ErrorCode.USER_DOES_KNOW_HAVE_THIS_APPLICATION), HttpStatus.OK);
        }
        Patient patient = new Patient();
        BeanUtils.copyProperties(patientDto.getPatient(), patient);
        try {
            Patient result = patientService.createPatient(patient, patientDto.getApplicationToken());
            return new ResponseEntity<PatientResponse>(new PatientResponse(result), HttpStatus.OK);
        } catch (ApplicationDoesNotExistException e) {
            PatientResponse response = new PatientResponse(ErrorCode.APPLICATION_UNKNOWN);
            return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Patient update")
    @RequestMapping(value = "/patient/update", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<PatientResponse> updatePatient(@RequestBody PatientDTO patientDto) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(patientDto.getUtilisateurToken());
        if (utilisateur == null) {
            return new ResponseEntity<PatientResponse>(new PatientResponse(ErrorCode.USER_UNKONWN), HttpStatus.OK);
        }
        if (!applicationService.checkApplicationBelongsUtilisateur(patientDto.getApplicationToken(),
                patientDto.getUtilisateurToken())) {
            return new ResponseEntity<PatientResponse>(
                    new PatientResponse(ErrorCode.USER_DOES_KNOW_HAVE_THIS_APPLICATION), HttpStatus.OK);
        }
        Patient patient = patientRepository.findByToken(patientDto.getPatient().getToken());
        if (patient == null) {
            PatientResponse response = new PatientResponse(ErrorCode.PATIENT_UNKNOWN);
            return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
        }

        BeanUtils.copyProperties(patientDto.getPatient(), patient);
        try {
            Patient result = patientService.updatePatient(patient, patientDto.getApplicationToken());
            return new ResponseEntity<PatientResponse>(new PatientResponse(result), HttpStatus.OK);
        } catch (ApplicationDoesNotExistException e) {
            PatientResponse response = new PatientResponse(ErrorCode.APPLICATION_UNKNOWN);
            return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Patient load")
    @RequestMapping(value = "/patient/load", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<PatientResponse> loadPatient(@RequestBody PatientDTO patientDto) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(patientDto.getUtilisateurToken());
        if (utilisateur == null) {
            return new ResponseEntity<PatientResponse>(new PatientResponse(ErrorCode.USER_UNKONWN), HttpStatus.OK);
        }
        if (!applicationService.checkApplicationBelongsUtilisateur(patientDto.getApplicationToken(),
                patientDto.getUtilisateurToken())) {
            return new ResponseEntity<PatientResponse>(
                    new PatientResponse(ErrorCode.USER_DOES_KNOW_HAVE_THIS_APPLICATION), HttpStatus.OK);
        }
        Patient patient = patientRepository.findByToken(patientDto.getPatient().getToken());
        if (patient == null) {
            PatientResponse response = new PatientResponse(ErrorCode.PATIENT_UNKNOWN);
            return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
        }

        return new ResponseEntity<PatientResponse>(new PatientResponse(patient), HttpStatus.OK);

    }

    @ApiOperation(value = "Patient liste")
    @RequestMapping(value = "/patient/list", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<PatientsResponse> listPatient(@RequestBody PatientSearchDTO patientSearchDto) {
        Utilisateur utilisateur = utilisateurRepository.findByToken(patientSearchDto.getUtilisateurToken());
        if (utilisateur == null) {
            return new ResponseEntity<PatientsResponse>(new PatientsResponse(ErrorCode.USER_UNKONWN), HttpStatus.OK);
        }
        if (!applicationService.checkApplicationBelongsUtilisateur(patientSearchDto.getApplicationToken(),
                patientSearchDto.getUtilisateurToken())) {
            return new ResponseEntity<PatientsResponse>(
                    new PatientsResponse(ErrorCode.USER_DOES_KNOW_HAVE_THIS_APPLICATION), HttpStatus.OK);
        }
        List<Patient> patients = patientRepository.findAllByApplication(patientSearchDto.getApplicationToken());

        PatientsResponse response = new PatientsResponse(patients);
        return new ResponseEntity<PatientsResponse>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Ajout d'une ordonnance")
    @RequestMapping(value = "/ordonnance/new", method = RequestMethod.POST, produces = "application/json")
    @CrossOrigin(origins = "*")
    public ResponseEntity<OrdonnanceResponse> addOrdonnance(@RequestBody OrdonnanceDTO ordonnanceDto) {

        Utilisateur utilisateur = utilisateurRepository.findByToken(ordonnanceDto.getUtilisateurToken());
        if (utilisateur == null) {
            return new ResponseEntity<OrdonnanceResponse>(new OrdonnanceResponse(ErrorCode.USER_UNKONWN),
                    HttpStatus.OK);
        }
        if (!applicationService.checkApplicationBelongsUtilisateur(ordonnanceDto.getApplicationToken(),
                ordonnanceDto.getUtilisateurToken())) {
            return new ResponseEntity<OrdonnanceResponse>(
                    new OrdonnanceResponse(ErrorCode.USER_DOES_KNOW_HAVE_THIS_APPLICATION), HttpStatus.OK);
        }

        Patient patient = patientRepository.findByToken(ordonnanceDto.getPatientToken());
        if (patient == null) {
            return new ResponseEntity<OrdonnanceResponse>(new OrdonnanceResponse(ErrorCode.PATIENT_UNKNOWN),
                    HttpStatus.OK);
        }

        OrdonnanceResponse ordonnanceResponse = new OrdonnanceResponse();

        Ordonnance ordonnance;
        try {
            ordonnance = patientService.createOrdonance(patient, ordonnanceDto.getApplicationToken(),
                    ordonnanceDto.getOrdonnance().getDateDebut(), ordonnanceDto.getOrdonnance().getDateFin());
            ordonnanceResponse.setOrdonnance(ordonnance);
        } catch (ApplicationDoesNotExistException e) {
            ordonnanceResponse.setError(ErrorCode.APPLICATION_UNKNOWN);
        }

        return new ResponseEntity<OrdonnanceResponse>(ordonnanceResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Ajout un fichier a une ordonnance")
    @RequestMapping(value = "/ordonnance/new/file", method = RequestMethod.POST, produces = "application/json")
    @CrossOrigin(origins = "*")
    public ResponseEntity<FileResponse> addFileToOrdonnance(@RequestParam("applicationToken") String applicationToken,
            @RequestParam("ordonnanceToken") String ordonnanceToken, @RequestParam("source") MultipartFile source) {
        FileResponse fileResponse = new FileResponse();

        try {
            SecuredFile securedFile = patientService.addFileOrdonance(applicationToken, ordonnanceToken,
                    source.getContentType(), IOUtils.toByteArray(source.getInputStream()));
            fileResponse.setToken(securedFile.getToken());

        } catch (ApplicationDoesNotExistException e) {
            fileResponse.setError(ErrorCode.APPLICATION_UNKNOWN);
        } catch (IOException e) {
            fileResponse.setError(ErrorCode.FILE_UPLOAD_ERROR);
        }
        return new ResponseEntity<FileResponse>(fileResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Populate")
    @RequestMapping(value = "/populate", produces = "application/json", method = RequestMethod.POST)
    @CrossOrigin(origins = "*")
    public ResponseEntity<HashMap<String, String>> populate() {
        HashMap<String, String> resp = new HashMap<String, String>();

        UserLoginDTO credentials = new UserLoginDTO();
        credentials.setEmail("damien@kerbart.com");
        credentials.setPassword("toto1234");
        UtilisateurResponse utilisateurResponse = this.signIn(credentials).getBody();

        Application app = this.createNewApplication(utilisateurResponse.getUtilisateur().getToken(), "My App")
                .getBody();
        PatientDTO patientDto1 = new PatientDTO();
        Patient p = new Patient();
        p.setNom("KERBART");
        p.setPrenom("Damien");
        patientDto1.setPatient(p);
        patientDto1.setUtilisateurToken(utilisateurResponse.getUtilisateur().getToken());
        patientDto1.setApplicationToken(app.getToken());
        Patient patient = this.createNewPatient(patientDto1).getBody().getPatient();

        String ordoToken = "";
        try {
            Ordonnance ordo = patientService.createOrdonance(patient, app.getToken(), new Date(), new Date());
            ordoToken = ordo.getToken();
        } catch (ApplicationDoesNotExistException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        resp.put("userToken", utilisateurResponse.getUtilisateur().getToken());
        resp.put("applicationToken", app.getToken());
        resp.put("patientToken", patient.getToken());
        resp.put("ordoToken", ordoToken);

        return new ResponseEntity<HashMap<String, String>>(resp, HttpStatus.OK);

    }

}
