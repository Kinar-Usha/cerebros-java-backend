package com.cerebros.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

public class ClientService {

	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
	private static final MockFMTS fmts = new MockFMTS();

	// ========== Fields and their Getters/Setters ==========
	private HashMap<String, Client> clients;
	Set<Preferences> preferences = new HashSet<>();

	boolean roboAdvisorTermsAccept = false;

	public HashMap<String, Client> getAllClients() {
		return clients;
	}

	public void setAllClients(HashMap<String, Client> clients) {
		this.clients = clients;
	}

	public Client getClient(String email) {
		return clients.get(email);
	}

//	private HashMap<String, String> emailToClientId;
//
//	public HashMap<String, String> getEmailToClientId() {
//		return emailToClientId;
//	}
//
//	public void setEmailToClientId(HashMap<String, String> emailToClientId) {
//		this.emailToClientId = emailToClientId;
//	}

	// ===================== Constructors =====================
	public ClientService() {
		super();
		setAllClients(new HashMap<String, Client>());
//		setEmailToClientId(new HashMap<String, String>());

	}

	// ======================== Methods =======================
	public boolean verifyEmailAddress(String email) {
		// DONE check the email address by regex
		// DONE If regex of email is fine, then check if it exists in the mock data
		// using MockFMTS
		// DONE return true or false here. The appropriate use of true or false here
		// depends on login or signup use case
		// DONE if the regex itself is invalid, throw error

		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Invalid Email Format");
		} else {
			if (clients.containsKey(email))
				return false;
		}

		return true;

	}

	public void registerClient(Person person, Set<ClientIdentification> clientIdentifications) {
		// TODO verify ClientIdentification
		// TODO generate UID
		// TODO add client to hashmaps

		boolean isIdentificationValid = true;
		for (ClientIdentification cid : clientIdentifications) {
			isIdentificationValid = fmts.verifyClientIdentification(cid);
			if (isIdentificationValid == false)
				throw new IllegalArgumentException("Cannot register client with invalid client identification");
		}

		String clientId = generateClientUID();
		System.out.println(clientId);

		Client client = new Client(clientId, person, clientIdentifications);
		clients.put(person.getEmail(), client);

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

		// Client A
		Person personA = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");
		ClientIdentification clientIdentificationA = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentificationsA = new HashSet<ClientIdentification>();
		clientIdentificationsA.add(clientIdentificationA);

		Client clientA = new Client("123", personA, clientIdentificationsA);

		// Client B
		Person personB = new Person("john.doe@gmail.com", LocalDate.of(1990, 5, 15), Country.USA, "90210");
		ClientIdentification clientIdentificationB = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"A1234567");
		Set<ClientIdentification> clientIdentificationsB = new HashSet<ClientIdentification>();
		clientIdentificationsB.add(clientIdentificationB);

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

		Client clientC = new Client("789", personC, clientIdentificationsC);

		// Add to clients
		clients.put("bhavesh@gmail.com", clientA);
		clients.put("john.doe@gmail.com", clientB);
		clients.put("jane.doe@gmail.com", clientC);

//		emailToClientId.put("bhavesh@gmail.com", "123");
//		emailToClientId.put("john.doe@gmail.com", "456");
//		emailToClientId.put("jane.doe@gmail.com", "789");

	}

	public void addPreferences(Preferences preference, Boolean roboAdvisorTermsAccept) throws Exception {

		if (roboAdvisorTermsAccept) {
			if (preference == null) {
				throw new NullPointerException("Preference cannot be null");
			}
			preferences.add(preference);

		} else {
			throw new Exception("Accept RoboAdvisor-Terms and Conditions");
		}

	}

	public Set<Preferences> getPreferences() {
		return preferences;
	}

	public void updatePreference(Preferences preference) throws Exception {

		if (preference == null) {
			throw new IllegalArgumentException("Preference cannot be null");
		}
		for (Preferences pref : preferences) {
			if (pref.getClientEmail().equals(preference.getClientEmail())) {
				preferences.remove(preference);
				preferences.add(preference);
				return;
			}
		}

		throw new Exception("Preference update failed");
	}

}
