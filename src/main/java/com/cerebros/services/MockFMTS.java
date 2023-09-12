package com.cerebros.services;

import java.util.HashMap;
import java.util.UUID;

import com.cerebros.models.Client;

public class MockFMTS {

	private HashMap<String, Client> clients;

	private String generateClientUID() {
		return UUID.randomUUID().toString();
	}

}
