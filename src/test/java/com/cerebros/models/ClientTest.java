package com.cerebros.models;

import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cerebros.contants.ClientIdentificationType;
import com.cerebros.contants.Country;

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

		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				"333-22-4444");
		Set<ClientIdentification> clientIdentifications = new HashSet<ClientIdentification>();
		clientIdentifications.add(clientIdentification);

		client = new Client("bhavesh@gmail.com", LocalDate.of(2001, 9, 6), Country.INDIA, "201014",
				clientIdentifications);

		assertNotNull(client);
	}

}
