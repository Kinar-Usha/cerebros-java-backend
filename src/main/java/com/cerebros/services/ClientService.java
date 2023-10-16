package com.cerebros.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cerebros.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.integration.doa.ClientDao;

import io.micrometer.core.ipc.http.HttpSender.Response;

@Service
public class ClientService {

	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

	// ========== Fields and their Getters/Setters ==========
	// private HashMap<String, Client> clients;

	boolean roboAdvisorTermsAccept = false;

	// ===================== Constructors =====================
	@Autowired
	private ClientDao dao;

	// @Autowired
	private FMTSService fmtsService;

	public ClientService() {
		super();
		// setAllClients(new HashMap<String, Client>());
		// setEmailToClientId(new HashMap<String, String>());
		// this.restTemplate = new RestTemplate();
		this.fmtsService = new FMTSService(new RestTemplate());

	}

	// ======================== Methods =======================

	public Client getClient(String clientId) {
		return dao.getClient(clientId);
	}

	public Client getClientFromEmail(String email) {
		return dao.getClientByEmail(email);
	}

	public boolean verifyEmailAddress(String email) {
		// Return false if email exists in DB

		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid Email Format");
		}
		return !dao.emailExists(email);
	}

	public int registerClient(Person person, Set<ClientIdentification> clientIdentifications, String password) {

		// Verify Identification with Regex
		boolean isIdentificationValid = true;
		for (ClientIdentification cid : clientIdentifications) {
			isIdentificationValid = verifyClientIdentification(cid);
			if (!isIdentificationValid)
				throw new IllegalArgumentException("Cannot register client with invalid client identification");
		}

		// Verify Email
		boolean isEmailValid = verifyEmailAddress(person.getEmail());

		if (!isEmailValid) {
			throw new ClientAlreadyExistsException("User with this email is already registered");
		}

		// Generate UID using FMTS
		String clientId = generateClientUID(person.getEmail());
		System.out.println(clientId);

		Client client = new Client(clientId, person, clientIdentifications);
		return dao.register(client, password);

		// Insert into DB

	}

	private String generateClientUID(String email) {
		// String clientId;
		// do {
		// clientId = UUID.randomUUID().toString();
		// } while (dao.clientIdExists(clientId));

		ResponseEntity<ClientRequest> response = fmtsService.getClientToken(new ClientRequest(email, "", ""));
		String clientId = response.getBody().getClientId();
		System.out.println(clientId);

		return clientId;

	}

	public int addPreferences(String clientId, Preferences preferences) {
		Preferences pref = dao.getClientPreferences(clientId);
		int added=0;
		if(pref==null) {
		added = dao.addClientPreferences(preferences, clientId);
		if (added == 0) {
			throw new RuntimeException("Failed to add preferences");
		}
		}
		else {
			
			added=dao.updateClientPreferences(preferences, clientId);
			if (added == 0) {
				throw new RuntimeException("Failed to add preferences");
			}
		}

		return added;
	}

	public int updatePreferences(String clientId, Preferences preference) {
		if (preference == null) {
			throw new IllegalArgumentException("Preference cannot be null");
		}

		int updatedRows = dao.updateClientPreferences(preference, clientId);
		if (updatedRows == 0) {
			throw new RuntimeException("Failed to update preferences");
		}
		return updatedRows;

	}

	public boolean login(String email, String password) {
		boolean isCredsValid = dao.login(email, password);
		if (!isCredsValid) {
			throw new InvalidCredentialsException("Invalid Credentials");
		}
		return isCredsValid;
	}

	// ======= Helpers =======
	private boolean verifyClientIdentification(ClientIdentification clientIdentification) {
		// verify the syntax of each type of indentification
		// SSN = 111-22-3333
		// Aadhaar = 1234-4567-7890
		// Passport = A1234567

		String identificationNumber = clientIdentification.getValue();
		ClientIdentificationType identificationType = clientIdentification.getType();

		switch (identificationType) {
			case SSN:
				// Verify SSN syntax
				if (identificationNumber.matches("\\d{3}-\\d{2}-\\d{4}")) {
					return true;
				}
				break;
			case AADHAAR:
				// Verify Aadhaar syntax
				if (identificationNumber.matches("\\d{4}-\\d{4}-\\d{4}")) {
					return true;
				}
				break;
			case PASSPORT:
				// Verify Passport syntax
				if (identificationNumber.matches("[A-Z]\\d{7}")) {
					return true;
				}
				break;

			default:
				break;
		}

		return false;
	}

	// Mock Method (To Be Removed in future)
	public void setupMockClients() {

		// Client A bhavesh@gmail.com john.doe@gmail.com
		Person personA = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");
		ClientIdentification clientIdentificationA = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentificationsA = new HashSet<ClientIdentification>();
		clientIdentificationsA.add(clientIdentificationA);
		Preferences preferenceA = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		Client clientA = new Client("123", personA, clientIdentificationsA);

		// Client B
		Person personB = new Person("john.doe@gmail.com", LocalDate.of(1990, 5, 15), Country.USA, "90210");
		ClientIdentification clientIdentificationB = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"A1234567");
		Set<ClientIdentification> clientIdentificationsB = new HashSet<ClientIdentification>();
		clientIdentificationsB.add(clientIdentificationB);
		Preferences preferenceB = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		Client clientB = new Client("456", personB, clientIdentificationsB);

		// Client C
		Person personC = new Person("jane.doe@gmail.com", LocalDate.of(1995, 2, 28), Country.IRELAND, "M5V 2L7");
		ClientIdentification clientIdentificationC1 = new ClientIdentification(ClientIdentificationType.SSN,
				"333-21-4444");
		ClientIdentification clientIdentificationC2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentificationsC = new HashSet<ClientIdentification>();
		clientIdentificationsC.add(clientIdentificationC1);
		clientIdentificationsC.add(clientIdentificationC2);
		Preferences preferenceC = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		Client clientC = new Client("789", personC, clientIdentificationsC);

		// // Add to clients
		// clients.put("123", clientA);
		// clients.put("456", clientB);
		// clients.put("789", clientC);

		// emailToClientId.put("bhavesh@gmail.com", "123");
		// emailToClientId.put("john.doe@gmail.com", "456");
		// emailToClientId.put("jane.doe@gmail.com", "789");

		// // Add preferences
		// addPreferences("123", preferenceA, true);
		// addPreferences("456", preferenceB, true);
		// addPreferences("789", preferenceC, true);

	}

	public Preferences getPreferences(String clientId) {
		if (clientId == "") {
			throw new IllegalArgumentException();
		}
		Preferences pref = dao.getClientPreferences(clientId);
		System.out.println(pref+"HI");
		return pref;
	}

	public int addCash(String clientId, BigDecimal cash){
		return  dao.insertCash(clientId,cash);
	}
	public Cash getCash(String  clientId){
		return dao.getCashRemaining(clientId);
	}
}
