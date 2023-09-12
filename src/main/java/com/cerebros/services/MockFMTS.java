package com.cerebros.services;

import java.util.HashMap;

import com.cerebros.models.Client;
import com.cerebros.models.ClientIdentification;

public class MockFMTS {

	private HashMap<String, Client> clients;

	public boolean verifyClientIdentification(ClientIdentification clientIdentification) {

		// TODO
		// verify the syntax of each type of indentification based on country
		// SSN = 111-22-3333
		// Aadhaar = 1234-4567-7890
		// Passport = A1234567

		return true;
	}

}
