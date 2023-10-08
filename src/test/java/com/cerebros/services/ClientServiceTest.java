package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;

import org.mockito.*;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientAlreadyExistsException;
import com.cerebros.exceptions.InvalidCredentialsException;
import com.cerebros.integration.doa.ClientDao;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ClientServiceTest {

	@Mock
	ClientDao clientDao;

	@Autowired
	@InjectMocks
	private ClientService clientService;

	private Person person;
	private ClientIdentification clientIdentification;
	private Set<ClientIdentification> clientIdentifications;
	private Client client;
	private Preferences preferences;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		// Sample client data
		person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		preferences = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");

		client = new Client("123", person, clientIdentifications);

	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest
	@ValueSource(strings = { "bhavesh@gmail.com", "john.doe@gmail.com", "jane.doe@gmail.com" })
	void verifyExistingEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		// Should return false for emails that already exist in the clients
		assertFalse(clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "Kin.s@gmail.com", "k1@gmail.com", "A_kinar@yahoo.com" })
	void TestVerifyEmailAdressFormat_success(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		assertDoesNotThrow(() -> clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(true);
		assertThrows(IllegalArgumentException.class, () -> clientService.verifyEmailAddress(email));
	}

	@ParameterizedTest
	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
	void verifyNonexistentEmailAddress(String email) {
		Mockito.when(clientDao.emailExists(email)).thenReturn(false);
		// Should return true for emails that are not registered yet
		assertTrue(clientService.verifyEmailAddress(email));
	}

	@Test
	void registerInvalidClientIdentification() {
		clientIdentification.setValue("333-22-44544");
		clientIdentifications.add(clientIdentification);

		assertThrows(IllegalArgumentException.class,
				() -> clientService.registerClient(person, clientIdentifications, "1234"));
	}

	@Test
	void registerValidClient() throws SQLException {

		Mockito.when(clientDao.register(client, "1234")).thenReturn(1);

		assertEquals(1, clientService.registerClient(person, clientIdentifications, "1234"));
	}

	@Test
	void registrationFailsOnExistingClientEmail() {

		Mockito.when(clientDao.emailExists(person.getEmail())).thenReturn(true);

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications, "123456"));
	}

	@Test
	void registrationAddsExistingClientWithNewEmail() {

		Mockito.doThrow(ClientAlreadyExistsException.class).when(clientDao).register(Mockito.any(Client.class),
				Mockito.anyString());

		assertThrows(ClientAlreadyExistsException.class,
				() -> clientService.registerClient(person, clientIdentifications, "123456"));
	}

	@Test
	void testAddPreference() throws Exception {

		Mockito.when(clientDao.getClient(client.getClientId())).thenReturn(client);
		Mockito.when(clientDao.addClientPreferences(preferences,
				client.getClientId())).thenReturn(1);

		assertDoesNotThrow(() -> clientService.addPreferences(client.getClientId(), preferences, true));
	}

	@Test
	public void testAddPreferencesWithNullPreference() {
		Mockito.doThrow(NullPointerException.class).when(clientDao).addClientPreferences(null,
				"123");
		assertThrows(NullPointerException.class, () -> clientService.addPreferences("123", null, true));
	}

	@Test
	public void testAddPreferencesWithoutAcceptingTerms() {
		Preferences preferences = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		assertThrows(RuntimeException.class, () -> clientService.addPreferences("123", preferences, false));

	}

	@Test
	public void testUpdatePreferenceWithExistingPreference() throws Exception {

		Preferences newPreference = new Preferences("Retirement", "High", "1-3 years", "Less than $50,000");

		Mockito.when(clientDao.updateClientPreferences(newPreference, "123")).thenReturn(1);

		assertDoesNotThrow(() -> clientService.updatePreferences("123", newPreference));
	}

	@Test
	public void testUpdatePreferenceWithNullPreference() {

		Mockito.doThrow(NullPointerException.class).when(clientDao).updateClientPreferences(null, "123");

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> clientService.updatePreferences("123", null));
		assertEquals("Preference cannot be null", exception.getMessage());
	}

	@Test
	public void invalidEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-44445";

		Mockito.when(clientDao.login(email, password)).thenReturn(false);

		assertThrows(InvalidCredentialsException.class, () -> clientService.login(email, password));
	}

	@Test
	public void validEmailLogin() {

		String email = "bhavesh@gmail.com";
		String password = "333-22-4444";

		Mockito.when(clientDao.login(email, password)).thenReturn(true);

		assertTrue(clientService.login(email, password));
	}

}
