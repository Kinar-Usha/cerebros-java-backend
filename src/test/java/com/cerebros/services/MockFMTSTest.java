package com.cerebros.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.cerebros.constants.ClientIdentificationType;
import com.cerebros.models.ClientIdentification;

class MockFMTSTest {

	private MockFMTS mockFMTS;

	@BeforeEach
	void setUp() throws Exception {
		mockFMTS = new MockFMTS();
	}

	@AfterEach
	void tearDown() throws Exception {
		mockFMTS = null;
	}

	@ParameterizedTest
	@ValueSource(strings = { "333-22-4444", "123-45-6789", "987-65-4321" })
	void testVerifyClientIdentificationSSN(String ssn) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN, ssn);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertTrue(result);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1234-5678-9012", "1111-2222-3333", "9999-8888-7777" })
	void testVerifyClientIdentificationAadhaar(String aadhaar) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.AADHAAR, aadhaar);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertTrue(result);
	}

	@ParameterizedTest
	@ValueSource(strings = { "A1234567", "B9876543", "C4567890" })
	void testVerifyClientIdentificationPassport(String passport) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.PASSPORT,
				passport);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertTrue(result);
	}

	@ParameterizedTest
	@ValueSource(strings = { "123-s45-6789", "1234-3232", "123-45-67894", "1235-12-123", "2hdsjak4", "123 45 6789" })
	void testVerifyClientIdentificationInvalidSSN(String invalidIdentification) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.SSN,
				invalidIdentification);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertFalse(result);
	}

	@ParameterizedTest
	@ValueSource(strings = { "1234-5678-901", "1234-5678-90123", "1234-56-7890", "1234-5678-901a" })
	void testVerifyClientIdentificationInvalidAadhaar(String invalidIdentification) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.AADHAAR,
				invalidIdentification);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertFalse(result);
	}

	@ParameterizedTest
	@ValueSource(strings = { "A123467", "9876543", "C456d7890", "12345678" })
	void testVerifyClientIdentificationInvalidPassport(String invalidIdentification) {
		ClientIdentification clientIdentification = new ClientIdentification(ClientIdentificationType.PASSPORT,
				invalidIdentification);
		boolean result = mockFMTS.verifyClientIdentification(clientIdentification);
		assertFalse(result);
	}

	// TODO Add ID to Country match verification as well like AADHAAR is only valid
	// for INDIA

}
