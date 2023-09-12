package com.cerebros.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientTest {

	private Client client;

	@BeforeEach
	void setUp() throws Exception {
//		client = new Client(null, null, null, null, null);
	}

	@AfterEach
	void tearDown() throws Exception {
		client = null;
	}

	@Test
	void test() {
		assertNotNull(client);
	}

}
