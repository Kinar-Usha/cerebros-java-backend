package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
	@ValueSource(strings = { "sadsad", "invalidemail@", "@invalidemail.com", "invalidemail.com", "ds@dsd" })
	void verifyInvalidEmailAddress(String email) {
		boolean emailExists = clientService.verifyEmailAddress(email);
		assertFalse(emailExists);
	}

	// TODO to be done by bhavesh based on mock fmts
//	@ParameterizedTest
//	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
//	void verifyNonexistentEmailAddress(String email) {
//		boolean emailExists = clientService.verifyEmailAddress(email);
//		assertFalse(emailExists);
//	}

}
