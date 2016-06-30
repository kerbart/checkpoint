package com.kerbart.checkpoint.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kerbart.checkpoint.controller.dto.CabinetDTO;
import com.kerbart.checkpoint.controller.dto.CommentaireDTO;
import com.kerbart.checkpoint.controller.dto.CommonDTO;
import com.kerbart.checkpoint.controller.dto.OrdonnanceDTO;
import com.kerbart.checkpoint.controller.dto.PatientDTO;
import com.kerbart.checkpoint.controller.dto.PatientSearchDTO;
import com.kerbart.checkpoint.controller.dto.UserLoginDTO;
import com.kerbart.checkpoint.controller.responses.CabinetJoinResponse;
import com.kerbart.checkpoint.controller.responses.CabinetResponse;
import com.kerbart.checkpoint.controller.responses.CabinetsResponse;
import com.kerbart.checkpoint.controller.responses.CommentaireResponse;
import com.kerbart.checkpoint.controller.responses.CommentairesResponse;
import com.kerbart.checkpoint.controller.responses.ErrorCode;
import com.kerbart.checkpoint.controller.responses.FileResponse;
import com.kerbart.checkpoint.controller.responses.NotificationResponse;
import com.kerbart.checkpoint.controller.responses.OrdonnanceResponse;
import com.kerbart.checkpoint.controller.responses.OrdonnancesResponse;
import com.kerbart.checkpoint.controller.responses.PatientResponse;
import com.kerbart.checkpoint.controller.responses.PatientsResponse;
import com.kerbart.checkpoint.controller.responses.UtilisateurResponse;
import com.kerbart.checkpoint.exceptions.CabinetDoesNotExistException;
import com.kerbart.checkpoint.exceptions.GlobalCredentialsException;
import com.kerbart.checkpoint.exceptions.UserAlreadyAssociatedException;
import com.kerbart.checkpoint.exceptions.UserAlreadyExistsException;
import com.kerbart.checkpoint.exceptions.UserDoesNotExistException;
import com.kerbart.checkpoint.exceptions.UserDoesNotHaveThisCabinetException;
import com.kerbart.checkpoint.model.Cabinet;
import com.kerbart.checkpoint.model.Commentaire;
import com.kerbart.checkpoint.model.Ordonnance;
import com.kerbart.checkpoint.model.Patient;
import com.kerbart.checkpoint.model.SecuredFile;
import com.kerbart.checkpoint.model.Utilisateur;
import com.kerbart.checkpoint.repositories.CabinetRepository;
import com.kerbart.checkpoint.repositories.NotificationUtilisateurRepository;
import com.kerbart.checkpoint.repositories.PatientRepository;
import com.kerbart.checkpoint.repositories.UtilisateurRepository;
import com.kerbart.checkpoint.services.CabinetService;
import com.kerbart.checkpoint.services.PatientService;
import com.kerbart.checkpoint.services.UtilisateurService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/api/")
@Api(value = "Checkpoint API")
public class ApiController {

//	https://www.ovh.com/cgi-bin/sms/http2sms.cgi?smsAccount=sms-kd273-1&login=checkpoint&password=test1234&from=CHECKPOINT&to=0033787879784&noStop=1&contentType=text/xml&message=Bonjour
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

	@Inject
	UtilisateurService utilisateurService;

	@Inject
	UtilisateurRepository utilisateurRepository;

	@Inject
	CabinetService cabinetService;

	@Inject
	PatientService patientService;

	@Inject
	PatientRepository patientRepository;

	@Inject
	CabinetRepository cabinetRepository;

	@Inject
	NotificationUtilisateurRepository notificationUtilisateurRepository;
	
	@ApiOperation(value = "Connexion de l'utilisateur")
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

	@ApiOperation(value = "Création d'un compte pour un utilisateur donné")
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

	@ApiOperation(value = "Vérification du token d'un utilisateur")
	@RequestMapping(value = "/user/{token}/check", produces = "application/json", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public ResponseEntity<UtilisateurResponse> checkUser(@PathVariable("token") String token) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(token);
		UtilisateurResponse response = new UtilisateurResponse();
		if (utilisateur != null) {
			response.setUtilisateur(utilisateur);
			return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);
		} else {
			response.setError(ErrorCode.USER_TOKEN_UNKNOWN);
			return new ResponseEntity<UtilisateurResponse>(response, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "Liste les notifications pour un utilisateur donné")
	@RequestMapping(value = "/user/notifications", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<NotificationResponse> listNotifications(@RequestBody CommonDTO commonDto) {
		NotificationResponse response = new NotificationResponse();
		try {
			checkCabinetAndUtilisateur(commonDto.getCabinetToken(), commonDto.getUtilisateurToken());
		} catch (GlobalCredentialsException e1) {
			response.setError(e1.getErrorCode());
			return new ResponseEntity<NotificationResponse>(response, HttpStatus.OK);
		}

		response.setNotificationCommentaire(notificationUtilisateurRepository.findAllNewComments(commonDto.getUtilisateurToken(), commonDto.getCabinetToken()));
		response.setNotificationOrdonnance( notificationUtilisateurRepository.findAllNewOrdonnances(commonDto.getUtilisateurToken(), commonDto.getCabinetToken()));
		response.setNotificationPatients(notificationUtilisateurRepository.findAllNewPatients(commonDto.getUtilisateurToken(), commonDto.getCabinetToken()));
		return new ResponseEntity<NotificationResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Liste des cabinets pour un utilisateur donné")
	@RequestMapping(value = "/user/{token}/cabinet/list", produces = "application/json", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public ResponseEntity<CabinetsResponse> listMyApplication(@PathVariable("token") String token) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(token);
		CabinetsResponse response = new CabinetsResponse();
		if (utilisateur != null) {
			List<Cabinet> list = cabinetService.getApplicationByUtilisateur(utilisateur.getToken());
			response.setCabinets(list);
			return new ResponseEntity<CabinetsResponse>(response, HttpStatus.OK);
		} else {
			response.setError(ErrorCode.USER_UNKNOWN);
			return new ResponseEntity<CabinetsResponse>(response, HttpStatus.OK);
		}
	}
	
	
	

	@ApiOperation(value = "Cabient courant pour un utilisateur donné")
	@RequestMapping(value = "/user/{token}/cabinet/current", produces = "application/json", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public ResponseEntity<CabinetResponse> getCurrentApplication(@PathVariable("token") String token) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(token);
		CabinetResponse response = new CabinetResponse();
		if (utilisateur != null) {
			Cabinet theCurrentApp = cabinetService.getCurrentCabinet(utilisateur);
			response.setCabinet(theCurrentApp);
			return new ResponseEntity<CabinetResponse>(response, HttpStatus.OK);
		} else {
			response.setError(ErrorCode.USER_UNKNOWN);
			return new ResponseEntity<CabinetResponse>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "Change le cabinet courant pour un utilisateur")
	@RequestMapping(value = "/user/cabinet/change", produces = "application/json", method = RequestMethod.GET)
	@CrossOrigin(origins = "*")
	public ResponseEntity<CabinetResponse> changeCurrentApplication(@RequestBody CabinetDTO cabinetDto,
			@PathVariable("appToken") String appToken) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(cabinetDto.getUtilisateurToken());
		CabinetResponse response = new CabinetResponse();

		try {
			if (utilisateur != null) {
				Cabinet theCurrentApp = cabinetService.changeCurrentCabinet(utilisateur, appToken);
				response.setCabinet(theCurrentApp);
				return new ResponseEntity<CabinetResponse>(response, HttpStatus.OK);
			} else {
				response.setError(ErrorCode.USER_UNKNOWN);
				return new ResponseEntity<CabinetResponse>(response, HttpStatus.OK);
			}
		} catch (CabinetDoesNotExistException e) {
			response.setError(ErrorCode.CABINET_UNKNOWN);
			return new ResponseEntity<CabinetResponse>(response, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "App creation")
	@RequestMapping(value = "/user/{token}/cabinet/create", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<Cabinet> createNewApplication(@PathVariable("token") String token,
			@RequestBody String appName) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(token);
		Cabinet application = null;
		if (utilisateur != null) {
			cabinetService.resetAllCabinetToNotCurrent(token);
			application = cabinetService.createCabinet(appName);
			try {
				cabinetService.associateCabinetToUser(application, utilisateur);
			} catch (UserAlreadyAssociatedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Cabinet>(application, HttpStatus.OK);
	}

	@ApiOperation(value = "App join")
	@RequestMapping(value = "/user/{token}/cabinet/join", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<CabinetJoinResponse> joinApplication(@PathVariable("token") String token,
			@RequestBody String shortCode) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(token);
		CabinetJoinResponse response = new CabinetJoinResponse();
		if (utilisateur != null) {
			try {
				Cabinet cabinet = cabinetService.associateCabinetToUser(shortCode, utilisateur);
				response.setCabinet(cabinet);
			} catch (UserAlreadyAssociatedException e) {
				response.setError(ErrorCode.USER_ALREADY_OWN_THIS_CABINET);
			} catch (CabinetDoesNotExistException e) {
				response.setError(ErrorCode.CABINET_UNKNOWN);
			}
		}
		return new ResponseEntity<CabinetJoinResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Patient creation")
	@RequestMapping(value = "/patient/create", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<PatientResponse> createNewPatient(@RequestBody PatientDTO patientDto) {
		try {
			checkCabinetAndUtilisateur(patientDto.getCabinetToken(), patientDto.getUtilisateurToken());
		} catch (GlobalCredentialsException e1) {
			return new ResponseEntity<PatientResponse>(new PatientResponse(e1.getErrorCode()), HttpStatus.OK);
		}

		Patient patient = new Patient();
		BeanUtils.copyProperties(patientDto.getPatient(), patient);
		Patient result = patientService.createPatient(patient, patientDto.getCabinetToken());
		return new ResponseEntity<PatientResponse>(new PatientResponse(result), HttpStatus.OK);

	}

	@ApiOperation(value = "Patient update")
	@RequestMapping(value = "/patient/update", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<PatientResponse> updatePatient(@RequestBody PatientDTO patientDto) {
		PatientResponse response = new PatientResponse();
		try {
			checkCabinetAndUtilisateur(patientDto.getCabinetToken(), patientDto.getUtilisateurToken());
		} catch (GlobalCredentialsException e1) {
			response.setError(e1.getErrorCode());
			return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
		}

		Patient patient = patientRepository.findByToken(patientDto.getPatient().getToken());
		if (patient == null) {
			response.setError(ErrorCode.PATIENT_UNKNOWN);
			return new ResponseEntity<PatientResponse>(response, HttpStatus.OK);
		}

		BeanUtils.copyProperties(patientDto.getPatient(), patient);
		Patient result = patientService.updatePatient(patient, patientDto.getCabinetToken(), patientDto.getUtilisateurToken());
		return new ResponseEntity<PatientResponse>(new PatientResponse(result), HttpStatus.OK);

	}

	@ApiOperation(value = "Patient load")
	@RequestMapping(value = "/patient/load", produces = "application/json", method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public ResponseEntity<PatientResponse> loadPatient(@RequestBody PatientDTO patientDto) {
		Utilisateur utilisateur = utilisateurRepository.findByToken(patientDto.getUtilisateurToken());
		if (utilisateur == null) {
			return new ResponseEntity<PatientResponse>(new PatientResponse(ErrorCode.USER_UNKNOWN), HttpStatus.OK);
		}
		if (!cabinetService.checkApplicationBelongsUtilisateur(patientDto.getCabinetToken(),
				patientDto.getUtilisateurToken())) {
			return new ResponseEntity<PatientResponse>(new PatientResponse(ErrorCode.USER_DOES_NOT_OWN_THIS_CABINET),
					HttpStatus.OK);
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
			return new ResponseEntity<PatientsResponse>(new PatientsResponse(ErrorCode.USER_UNKNOWN), HttpStatus.OK);
		}
		if (!cabinetService.checkApplicationBelongsUtilisateur(patientSearchDto.getCabinetToken(),
				patientSearchDto.getUtilisateurToken())) {
			return new ResponseEntity<PatientsResponse>(new PatientsResponse(ErrorCode.USER_DOES_NOT_OWN_THIS_CABINET),
					HttpStatus.OK);
		}
		List<Patient> patients = patientRepository.findAllByCabinet(patientSearchDto.getCabinetToken());

		PatientsResponse response = new PatientsResponse(patients);
		return new ResponseEntity<PatientsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Ajout d'une ordonnance")
	@RequestMapping(value = "/ordonnance/new", method = RequestMethod.POST, produces = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity<OrdonnanceResponse> addOrdonnance(@RequestBody OrdonnanceDTO ordonnanceDto) {

		Utilisateur utilisateur = utilisateurRepository.findByToken(ordonnanceDto.getUtilisateurToken());
		if (utilisateur == null) {
			return new ResponseEntity<OrdonnanceResponse>(new OrdonnanceResponse(ErrorCode.USER_UNKNOWN),
					HttpStatus.OK);
		}
		if (!cabinetService.checkApplicationBelongsUtilisateur(ordonnanceDto.getCabinetToken(),
				ordonnanceDto.getUtilisateurToken())) {
			return new ResponseEntity<OrdonnanceResponse>(
					new OrdonnanceResponse(ErrorCode.USER_DOES_NOT_OWN_THIS_CABINET), HttpStatus.OK);
		}

		Patient patient = patientRepository.findByToken(ordonnanceDto.getPatientToken());
		if (patient == null) {
			return new ResponseEntity<OrdonnanceResponse>(new OrdonnanceResponse(ErrorCode.PATIENT_UNKNOWN),
					HttpStatus.OK);
		}

		OrdonnanceResponse ordonnanceResponse = new OrdonnanceResponse();

		Ordonnance ordonnance;
		try {
			ordonnance = patientService.createOrdonance(utilisateur, patient, ordonnanceDto.getCabinetToken(),
					ordonnanceDto.getOrdonnance().getDateDebut(), ordonnanceDto.getOrdonnance().getDateFin(),
					ordonnanceDto.getOrdonnance().getCommentaire());
			ordonnanceResponse.setOrdonnance(ordonnance);
		} catch (CabinetDoesNotExistException e) {
			ordonnanceResponse.setError(ErrorCode.CABINET_UNKNOWN);
		}

		return new ResponseEntity<OrdonnanceResponse>(ordonnanceResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Ajout un fichier a une ordonnance")
	@RequestMapping(value = "/ordonnance/new/file", method = RequestMethod.POST, produces = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity<FileResponse> addFileToOrdonnance(@RequestParam("cabinetToken") String cabinetToken,
			@RequestParam("utilisateurToken") String utilisateurToken,
			@RequestParam("ordonnanceToken") String ordonnanceToken, @RequestParam("source") MultipartFile source) {
		FileResponse response = new FileResponse();
		try {
			checkCabinetAndUtilisateur(cabinetToken, utilisateurToken);
		} catch (GlobalCredentialsException e) {
			response.setError(e.getErrorCode());
			return new ResponseEntity<FileResponse>(response, HttpStatus.OK);
		}

		try {
			SecuredFile securedFile = patientService.addFileOrdonance(cabinetToken, ordonnanceToken,
					source.getContentType(), IOUtils.toByteArray(source.getInputStream()));
			response.setToken(securedFile.getToken());
		} catch (IOException e) {
			response.setError(ErrorCode.FILE_UPLOAD_ERROR);
		}
		return new ResponseEntity<FileResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Liste les ordonnances d'un patient")
	@RequestMapping(value = "/patient/ordonnances", method = RequestMethod.POST, produces = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity<OrdonnancesResponse> listOrdonnances(@RequestBody OrdonnanceDTO ordonnanceDTO) {
		OrdonnancesResponse response = new OrdonnancesResponse();

		try {
			checkCabinetAndUtilisateur(ordonnanceDTO.getCabinetToken(), ordonnanceDTO.getUtilisateurToken());
		} catch (GlobalCredentialsException e) {
			response.setError(e.getErrorCode());
			return new ResponseEntity<OrdonnancesResponse>(response, HttpStatus.OK);
		}

		List<Ordonnance> ordonnances = patientService.getOrdonnances(ordonnanceDTO.getPatientToken());
		response.setOrdonnances(ordonnances);

		return new ResponseEntity<OrdonnancesResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Récupère un fichier crypté en fonction de la clé du cabinet")
	@ResponseBody
	@RequestMapping(value = "/patient/ordonnance/photo/{cabinetToken}/{utilisateurToken}/{fileToken}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] ordonnanceFile(@PathVariable("cabinetToken") String cabinetToken,
			@PathVariable("utilisateurToken") String utilisateurToken, @PathVariable("fileToken") String fileToken) {
		try {
			checkCabinetAndUtilisateur(cabinetToken, utilisateurToken);
		} catch (GlobalCredentialsException e) {
			return null;
		}
		return patientService.getFileOrdonnance(fileToken, cabinetToken);

	}

	@ApiOperation(value = "Ajout d'un commentaire patient")
	@RequestMapping(value = "/commentaire/add", method = RequestMethod.POST, produces = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity<CommentaireResponse> addOrdonnance(@RequestBody CommentaireDTO commentaireDto) {
		CommentaireResponse commentaireResponse = new CommentaireResponse();

		try {
			checkCabinetAndUtilisateur(commentaireDto.getCabinetToken(), commentaireDto.getUtilisateurToken());
		} catch (GlobalCredentialsException e) {
			commentaireResponse.setError(e.getErrorCode());
			return new ResponseEntity<CommentaireResponse>(commentaireResponse, HttpStatus.OK);
		}

		Patient patient = patientRepository.findByToken(commentaireDto.getPatientToken());
		if (patient == null) {
			return new ResponseEntity<CommentaireResponse>(new CommentaireResponse(ErrorCode.PATIENT_UNKNOWN),
					HttpStatus.OK);
		}
		Utilisateur utilisateur = utilisateurRepository.findByToken(commentaireDto.getUtilisateurToken());
		Commentaire commentaire = patientService.createCommentaire(utilisateur, patient,
				commentaireDto.getCabinetToken(), commentaireDto.getCommentaire());
		commentaireResponse.setCommentaire(commentaire);

		return new ResponseEntity<CommentaireResponse>(commentaireResponse, HttpStatus.OK);
	}

	@ApiOperation(value = "Liste les commentaires d'un patient")
	@RequestMapping(value = "/patient/commentaires", method = RequestMethod.POST, produces = "application/json")
	@CrossOrigin(origins = "*")
	public ResponseEntity<CommentairesResponse> listCommentaires(@RequestBody CommentaireDTO commentaireDto) {
		CommentairesResponse response = new CommentairesResponse();
		try {
			checkCabinetAndUtilisateur(commentaireDto.getCabinetToken(), commentaireDto.getUtilisateurToken());
		} catch (GlobalCredentialsException e) {
			response.setError(e.getErrorCode());
			return new ResponseEntity<CommentairesResponse>(response, HttpStatus.OK);
		}
		List<Commentaire> commentaires = patientService.getCommentaires(commentaireDto.getPatientToken());
		response.setCommentaires(commentaires);
		return new ResponseEntity<CommentairesResponse>(response, HttpStatus.OK);
	}

	/**
	 * Vérifie si - un utilisateur existe - un cabinet existe - un utilisateur
	 * appartient bien à un cabinet
	 * 
	 * @param cabinetToken
	 * @param utilisateurToken
	 * @throws UserDoesNotExistException
	 * @throws UserDoesNotHaveThisCabinetException
	 */
	private void checkCabinetAndUtilisateur(String cabinetToken, String utilisateurToken)
			throws GlobalCredentialsException {
		// vérifie si le token utilisateur est bon
		Utilisateur utilisateur = utilisateurRepository.findByToken(utilisateurToken);
		if (utilisateur == null) {
			throw new GlobalCredentialsException("Cet utilisateur n'existe pas", ErrorCode.USER_UNKNOWN);
		}
		// vérifie si le token cabinet est bon
		Cabinet cabinet = cabinetRepository.findByToken(cabinetToken);
		if (cabinet == null) {
			throw new GlobalCredentialsException("Ce cabinet n'existe pas", ErrorCode.CABINET_UNKNOWN);
		}
		// vérifie si l'utilisateur possède bien ce cabinet
		if (!cabinetService.checkApplicationBelongsUtilisateur(cabinetToken, utilisateurToken)) {
			throw new GlobalCredentialsException("Cet utilisateur n'est pas lié à ce cabinet",
					ErrorCode.USER_DOES_NOT_OWN_THIS_CABINET);
		}
	}

}
