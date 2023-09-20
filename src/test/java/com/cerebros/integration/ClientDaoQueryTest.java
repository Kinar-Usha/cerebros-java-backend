package com.cerebros.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;
import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;
import com.cerebros.models.Person;
import com.cerebros.models.Preferences;

class ClientDaoQueryTest {

	private SimpleDataSource dataSource;
	private Connection connection;
	private ClientDao dao;

	private Person person1;
	private Client client1;
	private ClientIdentification clientIdentification1;

	@BeforeEach
	void setUp() throws Exception {
		dataSource = new SimpleDataSource();
		connection = dataSource.getConnection();
		dao = new ClientDaoImpl(dataSource);

		person1 = new Person("john.doe@gmail.com", LocalDate.of(2001, 9, 11), Country.INDIA, "600097");
		clientIdentification1 = new ClientIdentification(ClientIdentificationType.PASSPORT, "B7654321");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification1);
		Preferences preferenceA = new Preferences("Investment", "High", "Long-term", "High");
		client1 = new Client("YOUR_CLIENTID", person1, clientIdentifications);
	}

	@AfterEach
	void tearDown() throws Exception {
		dataSource.shutdown();
		connection.close();
	}

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

}
