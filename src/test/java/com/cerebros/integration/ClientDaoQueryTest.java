package com.cerebros.integration;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.exceptions.ClientNotFoundException;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:beans.xml")
class ClientDaoQueryTest {

	@Autowired
	private ClientDaoImpl dao;

	private Client client1;

	@BeforeEach
	void setUp() throws Exception {

		Person person1 = new Person("john.doe@gmail.com", LocalDate.of(2001, 9, 11), Country.INDIA, "600097");
		ClientIdentification clientIdentification1 = new ClientIdentification(ClientIdentificationType.PASSPORT,
				"B7654321");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification1);

		client1 = new Client("YOUR_CLIENTID", person1, clientIdentifications);
	}

	@AfterEach
	void tearDown() throws Exception {

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
		client1.setClientIdentifications(null);
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
		client1.setClientIdentifications(null);
		assertEquals(client1, actualClient);
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

}
