package com.cerebros.integration.doa;

import com.cerebros.models.Client;
import com.cerebros.models.Preferences;

public interface ClientDao {

	boolean emailExists(String email);

	void register(Client client, String password);

	boolean login(String email, String password);

	Client getClient(String clientId);

	Client getClientByEmail(String email);

	int addClientPreferences(Preferences preferences, String clientId);

	int updateClientPreferences(Preferences preferences, String clientId);

	Preferences getClientPreferences(String clientId);

}
