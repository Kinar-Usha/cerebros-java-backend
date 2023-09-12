package com.cerebros.services;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.cerebros.contants.ClientIdentificationType;
import com.cerebros.contants.Country;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;

public class ClientService {

	private static final String EMAIL_PATTERN="^[A-Za-z0-9+_.-]+@(.+)$";

	// ========== Fields and their Getters/Setters ==========
	private HashMap<String, Client> clients;

	public HashMap<String, Client> getClients() {
		return clients;
	}

	public void setClients(HashMap<String, Client> clients) {
		this.clients = clients;
	}

	private HashMap<String, String> emailToClientId;

	public HashMap<String, String> getEmailToClientId() {
		return emailToClientId;
	}

	public void setEmailToClientId(HashMap<String, String> emailToClientId) {
		this.emailToClientId = emailToClientId;
	}

	// ===================== Constructors =====================
	public ClientService() {
		super();
		setClients(new HashMap<String, Client>());
		setEmailToClientId(new HashMap<String, String>());
	}

	// ======================== Methods =======================
	public boolean verifyEmailAddress(String email) {
		Pattern pattern= Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(email);
		if(!matcher.matches()){
			throw new IllegalArgumentException("Invalid Email Format");
		}

		// DONE check the email address by regex
		// TODO If regex of email is fine, then check if it exists in the mock data
		// using MockFMTS
		// TODO return true or false here. The appropriate use of true or false here
		// depends on login or signup use case
		// TODO if the regex itself is invalid, return false
		return true;
	}

	public String registerClient(Person person, ClientIdentification clientIdentification) {
		// TODO verify ClientIdentification
		// TODO generate UID
		// TODO add client to hashmaps
		return "";
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
		ClientIdentification clientIdentificationA = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentificationsA = new HashSet<ClientIdentification>();
		clientIdentificationsA.add(clientIdentificationA);

		Client clientA = new Client("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014",
				clientIdentificationsA);

		// Client B
		ClientIdentification clientIdentificationB = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"A1234567");
		Set<ClientIdentification> clientIdentificationsB = new HashSet<ClientIdentification>();
		clientIdentificationsB.add(clientIdentificationB);

		Client clientB = new Client("john.doe@gmail.com", LocalDate.of(1990, 5, 15), Country.USA, "90210",
				clientIdentificationsB);

		// Client C
		ClientIdentification clientIdentificationC1 = new ClientIdentification(ClientIdentificationType.SSN,
				"333-21-4444");
		ClientIdentification clientIdentificationC2 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentificationsC = new HashSet<ClientIdentification>();
		clientIdentificationsC.add(clientIdentificationC1);
		clientIdentificationsC.add(clientIdentificationC2);

		Client clientC = new Client("jane.doe@gmail.com", LocalDate.of(1995, 2, 28), Country.IRELAND, "M5V 2L7",
				clientIdentificationsC);

		// Add to clients
		clients.put("bhavesh@gmail.com", clientA);
		clients.put("john.doe@gmail.com", clientB);
		clients.put("jane.doe@gmail.com", clientC);

		emailToClientId.put("bhavesh@gmail.com", "123");
		emailToClientId.put("john.doe@gmail.com", "456");
		emailToClientId.put("jane.doe@gmail.com", "789");

	}

}
