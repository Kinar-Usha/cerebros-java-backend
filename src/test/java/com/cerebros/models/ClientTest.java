package com.cerebros.models;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.constants.Country;

class ClientTest {

	private Client client;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void createClient() {

		Person person = new Person("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.USA, "201014");

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);
		Preferences preference = new Preferences("Retirement", "Low", "1-3 years", "Less than $50,000");
		client = new Client("123", person, clientIdentifications);
		client.setPreferences(preference);

		assertNotNull(client);
	}

}
