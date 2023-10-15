package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.cerebros.integration.doa.impl.ClientDaoImpl;
import com.cerebros.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.exceptions.DatabaseException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ClientDaoQueryTest {

	@Autowired
	private ClientDaoImpl dao;

	private Client client1;

	@BeforeEach
	void setUp() throws Exception {

		Person person1 = new Person("john.doe@gmail.com", LocalDate.of(2001, 9, 11), Country.INDIA, "600097");
		ClientIdentification clientIdentification1 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		ClientIdentification clientIdentification2 = new ClientIdentification(ClientIdentificationType.SSN,
				"333-12-4445");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification1);
		clientIdentifications.add(clientIdentification2);

		client1 = new Client("YOUR_CLIENTID", person1, clientIdentifications);
	}

	@AfterEach
	void tearDown() throws Exception {

	}

	@Test
	void testGetCashRemaining() {
		Cash cash = dao.getCashRemaining("YOUR_CLIENTID");
		assertNotNull(cash);
	}

	@Test
	void testInvalidClientCash() {
		assertThrows(DatabaseException.class, () -> {
			dao.getCashRemaining("Invalid_ClientID");
		});
	}

	// Email Exists or Not

	@Test
	void testEmailExistsTrue() {
		boolean emailExists = dao.emailExists("john.doe@gmail.com");
		assertTrue(emailExists);
	}

	@Test
	void testEmailExistsFalse() {
		boolean emailExists = dao.emailExists("jane.doe@gmail.com");
		assertFalse(emailExists);
	}

	// Get Client by ID

	@Test
	void testGetClientWithNonExistentId() {
		assertThrows(ClientNotFoundException.class, () -> dao.getClient("NO_ID"));
	}

	@Test
	void testGetClientReturnsClient1() {
		Client actualClient = dao.getClient("YOUR_CLIENTID");
		// client1.setClientIdentifications(null);
		assertEquals(client1, actualClient);
	}

	// Get Client by Email

	@Test
	void testGetClientByEmailWithNonExistentId() {
		assertNull(dao.getClientByEmail("jane.doe@gmail.com"));
	}

	@Test
	void testGetClientByEmailReturnsClient1() {
		Client actualClient = dao.getClientByEmail("john.doe@gmail.com");
		// client1.setClientIdentifications(null);
		assertEquals(client1, actualClient);
	}

	// Get client identifications

	@Test
	void testGetClientIdentificationsWithNonExistentId() {
		assertTrue(dao.getClientIdentifications("NO_ID").size() == 0);
	}

	@Test
	void testGetClientIdentifications() {
		Set<ClientIdentification> actualClientIdentifications = dao.getClientIdentifications("YOUR_CLIENTID");
		Set<ClientIdentification> expectedClientIdentifications = new HashSet<ClientIdentification>();
		expectedClientIdentifications.add(new ClientIdentification(ClientIdentificationType.PASSPORT, "B7654321"));
		expectedClientIdentifications.add(new ClientIdentification(ClientIdentificationType.SSN, "333-12-4445"));
		assertEquals(expectedClientIdentifications, actualClientIdentifications);
	}


	// Login

	@Test
	void testLoginWithInvalidEmail() {
		assertFalse(() -> dao.login("john@gmail.com", "1234567890"));
	}

	@Test
	void testLoginWithInvalidPassword() {
		assertFalse(() -> dao.login("john.doe@gmail.com", "1267890"));
	}

	@Test
	void testLoginWithValidCreds() {
		assertTrue(() -> dao.login("john.doe@gmail.com", "1234567890"));
	}

	@Test
	void testInstantiateObjectNotNull() {
		assertNotNull(dao);
	}

	@Test
	void testGetAllPreferencesById() {
		assertNotNull(dao.getClientPreferences("YOUR_CLIENTID"));
	}

	@Test
	void testGetPreferencesByIdEqualsPurposes() {
		Preferences pref = new Preferences("Investment", "High", "Long-term", "High");
		assertEquals(pref.getPurpose(), dao.getClientPreferences("YOUR_CLIENTID").getPurpose());
	}

	@Test
	void testGetPreferencesByEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			dao.getClientPreferences("");
		});
	}

	@Test
	void testGetPreferencesByInvalidId() {
		assertThrows(DatabaseException.class, () -> {
			dao.getClientPreferences("YOUR_CLIENTID123444");
		});
	}

}
