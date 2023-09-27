package com.cerebros.services;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.integration.doa.ClientDao;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

@Service
public class ClientService {

	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

	// ========== Fields and their Getters/Setters ==========
	private HashMap<String, Client> clients;

	boolean roboAdvisorTermsAccept = false;

	public HashMap<String, Client> getAllClients() {
		return clients;
	}

	public void setAllClients(HashMap<String, Client> clients) {
		this.clients = clients;
	}

	public Client getClient(String clientId) {
		Client client = clients.get(clientId);
		if (client == null)
			throw new NullPointerException("Client doesn't exists");
		return client;
	}

	public Client getClientFromEmail(String email) {
		String clientId = emailToClientId.get(email);
		return getClient(clientId);
	}

	private HashMap<String, String> emailToClientId;

	public HashMap<String, String> getEmailToClientId() {
		return emailToClientId;
	}

	public void setEmailToClientId(HashMap<String, String> emailToClientId) {
		this.emailToClientId = emailToClientId;
	}

	// ===================== Constructors =====================
	@Autowired
	private ClientDao dao;

	public ClientService() {
		super();
		setAllClients(new HashMap<String, Client>());
		setEmailToClientId(new HashMap<String, String>());

	}

	// ======================== Methods =======================
	public boolean verifyEmailAddress(String email) {
		// Return false if email exists in DB

		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid Email Format");
		}
		return !dao.emailExists(email);
	}

	public void registerClient(Person person, Set<ClientIdentification> clientIdentifications, String password) {

		// Verify Identification with Regex
		boolean isIdentificationValid = true;
		for (ClientIdentification cid : clientIdentifications) {
			isIdentificationValid = verifyClientIdentification(cid);
			if (isIdentificationValid == false)
				throw new IllegalArgumentException("Cannot register client with invalid client identification");
		}

		// Verify Email
		boolean isEmailValid = verifyEmailAddress(person.getEmail());

		if (!isEmailValid) {
			throw new ClientAlreadyExistsException("User with this email is already registered");
		}

		// Register client
		String clientId = generateClientUID();
		System.out.println(clientId);

		Client client = new Client(clientId, person, clientIdentifications);

		// Insert into DB
		dao.register(client, password);

	}

	private String generateClientUID() {
		String clientId;
		do {
			clientId = UUID.randomUUID().toString();
		} while (clients.containsKey(clientId));
		return clientId;
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

		// Add to clients
		clients.put("123", clientA);
		clients.put("456", clientB);
		clients.put("789", clientC);

		emailToClientId.put("bhavesh@gmail.com", "123");
		emailToClientId.put("john.doe@gmail.com", "456");
		emailToClientId.put("jane.doe@gmail.com", "789");

		// Add preferences
		addPreferences("123", preferenceA, true);
		addPreferences("456", preferenceB, true);
		addPreferences("789", preferenceC, true);

	}

	public void addPreferences(String clientId, Preferences preferences, Boolean roboAdvisorTermsAccept) {

		if (roboAdvisorTermsAccept) {
			if (preferences == null) {
				throw new NullPointerException("Preference cannot be null");
			}
			Client client = getClient(clientId);
			client.setPreferences(preferences);

		} else {
			throw new RuntimeException("Accept RoboAdvisor-Terms and Conditions");
		}
	}

	public void updatePreferences(String clientId, Preferences preference) {

		if (preference == null) {
			throw new IllegalArgumentException("Preference cannot be null");
		}
		Client client = getClient(clientId);
		client.setPreferences(preference);

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

}
