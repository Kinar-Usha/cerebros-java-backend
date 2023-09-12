package com.cerebros.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

	// TODO to be done by bhavesh based on mock fmts
//	@ParameterizedTest
//	@ValueSource(strings = { "nonexistentemail@test.com", "notfound@gmail.com", "missingemail@abc.com" })
//	void verifyNonexistentEmailAddress(String email) {
//		boolean emailExists = clientService.verifyEmailAddress(email);
//		assertFalse(emailExists);
//	}

}
