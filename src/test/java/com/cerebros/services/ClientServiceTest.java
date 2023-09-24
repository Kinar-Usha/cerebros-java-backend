package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClientServiceTest {

	private ClientService clientService;

	@BeforeEach
	void setUp() throws Exception {
		clientService = new ClientService();
		clientService.setupMockClients();
	}

	@AfterEach
	void tearDown() throws Exception {
		clientService = null;
	}

	@ParameterizedTest
	@ValueSource(strings = { "bhavesh@gmail.com", "john.doe@gmail.com", "jane.doe@gmail.com" })
	void verifyExistingEmailAddress(String email) {
		// Should return false for emails that already exist in the clients
		assertFalse(clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest

	@ValueSource(strings = { "Kin.s@gmail.com", "k1@gmail.com", "A_kinar@yahoo.com" })
	void TestVerifyEmailAdressFormat_success(String email) {
		assertDoesNotThrow(() -> clientService.verifyEmailAddress(email));

	}

	@ParameterizedTest
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) {
		assertThrows(IllegalArgumentException.class, () -> clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
	void verifyNonexistentEmailAddress(String email) {
		// Should return true for emails that are not registered yet
		assertTrue(clientService.verifyEmailAddress(email));
	}

	@Test
	void registerInvalidClient() {
		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.USA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-44544");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertThrows(IllegalArgumentException.class, () -> clientService.registerClient(person, clientIdentifications));
	}

	@Test
	void registerValidClient() {
		Person person = new Person("vishku@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-12-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertDoesNotThrow(() -> clientService.registerClient(person, clientIdentifications));
	}

	@Test
	void registrationAddsToMockClients() {

		int beforeClientsLength = clientService.getAllClients().size();

		Person person = new Person("vishku@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-12-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		clientService.registerClient(person, clientIdentifications);

		int afterClientsLength = clientService.getAllClients().size();

		assertEquals(beforeClientsLength + 1, afterClientsLength);
	}

	@Test
	void registrationAddsExistingClient() {

		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications));
	}

	@Test
	void registrationAddsExistingClientWithNewEmail() {

		Person person = new Person("bhavesh2@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications));
	}

	@Test
	void testAddPreference() throws Exception {
		Person personA = new Person("client@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");
		ClientIdentification clientIdentificationA = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentificationsA = new HashSet<ClientIdentification>();
		clientIdentificationsA.add(clientIdentificationA);
		Preferences preferenceA = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		Client clientA = new Client("123", personA, clientIdentificationsA);

		HashMap<String, Client> clients = clientService.getAllClients();
		clients.put("client@gmail.com", clientA);
		clientService.addPreferences("client@gmail.com", preferenceA, true);
		assertEquals(preferenceA, clientA.getPreferences());

	}

	@Test
	public void testAddPreferencesWithNullPreference() {
		assertThrows(NullPointerException.class, () -> clientService.addPreferences("123", null, true));
	}

	@Test
	public void testAddPreferencesWithoutAcceptingTerms() {
		Preferences preferences = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		assertThrows(RuntimeException.class, () -> clientService.addPreferences("123", preferences, false));

	}

	@Test
	public void testUpdatePreferenceWithExistingPreference() throws Exception {

//		Person personA = new Person("client@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");
//		ClientIdentification clientIdentificationA = new ClientIdentification(ClientIdentificationType.SSN,
//				"333-22-4444");
//		Set<ClientIdentification> clientIdentificationsA = new HashSet<ClientIdentification>();
//		clientIdentificationsA.add(clientIdentificationA);
//
//		Preferences preferenceA = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
//
//		Client clientA = new Client("A1234", personA, clientIdentificationsA);
//		clientService.getAllClients().put("A1234", clientA);
//		clientService.getEmailToClientId().put("client@gmail.com", "A1234");
//		clientService.addPreferences("A1234", preferenceA, true);

		Client client = clientService.getClient("123");

		Preferences newPreference = new Preferences("Retirement", "High", "1-3 years", "Less than $50,000");

		clientService.updatePreferences("123", newPreference);

		assertEquals(newPreference, client.getPreferences());
	}

	@Test
	public void testUpdatePreferenceWithNullPreference() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> clientService.updatePreferences("abc@gmail.com", null));
		assertEquals("Preference cannot be null", exception.getMessage());
	}

	@Test
	public void invalidEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-44445";

		assertThrows(InvalidCredentialsException.class, () -> clientService.login(email, password));
	}

	@Test
	public void validEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-4444";

		assertTrue(clientService.login(email, password));
	}

	@Test
	public void validEmailLoginWithSSN() {

		String email = "jane.doe@gmail.com";
		String password = "333-21-4444";

		assertTrue(clientService.login(email, password));
	}

	@Test
	public void validEmailLoginWithPassport() {

		String email = "jane.doe@gmail.com";
		String password = "B7654321";

		assertTrue(clientService.login(email, password));
	}

}
