package com.cerebros.integration;

import com.cerebros.models.Client;

public interface ClientMapper {

	Client getClientByEmail(String email);

	Client getClient(String clientId);

	int checkLoginCreds(String email, String password);
}
