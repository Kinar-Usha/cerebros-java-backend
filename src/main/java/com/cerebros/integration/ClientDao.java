package com.cerebros.integration;

import com.cerebros.models.Client;
import com.cerebros.models.Preferences;

public interface ClientDao {

	boolean verifyEmail(String email);

	void register(Client client);

	boolean login(String email, String password);

	Client getClient(String clientId);

	void addClientPreferences(Preferences preferences);

	void updateClientPreferences(Preferences preferences);

}
