package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cerebros.contants.ClientIdentificationType;
import com.cerebros.contants.Country;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

	private ClientService clientService;

	@BeforeEach
	void setUp() throws Exception {
		clientService = new ClientService();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest
	@ValueSource(strings = { "bhavesh@gmail.com", "kin.s@gmail.com", "abc@test.com", "reshmi.lDs@gma.com",
			"bl@a32.ml" })
	void verifyExistingEmailAddress(String email) {
		boolean emailExists = clientService.verifyEmailAddress(email);
		assertTrue(emailExists);
	}

	@ParameterizedTest
	@ValueSource(strings = {"Kin.s@gmail.com", "k1@gmail.com", "A_kinar@yahoo.com"})
	void TestVerifyEmailAdressFormat_success(String email){
		assertDoesNotThrow(()->clientService.verifyEmailAddress(email));

	}

	@ParameterizedTest
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) {
		assertThrows(IllegalArgumentException.class,()->clientService.verifyEmailAddress(email));
	}

	// TODO check if email exists in the clients mockdata
	@ParameterizedTest
	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
	void verifyNonexistentEmailAddress(String email) {
		fail("Not yet implemented");
	}

	@Test
	void registerInvalidClient() {
		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.USA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-44544");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertThrows(IllegalArgumentException.class, () -> clientService.registerClient(person, clientIdentification));
	}

	@Test
	void registerValidClient() {
		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		assertDoesNotThrow(() -> clientService.registerClient(person, clientIdentification));
	}

	@Test
	void registrationAddsToMockClients() {

		int beforeClientsLength = clientService.getClients().size();

		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		clientService.registerClient(person, clientIdentification);

		int afterClientsLength = clientService.getClients().size();

		assertEquals(beforeClientsLength + 1, afterClientsLength);
	}

}
