package com.cerebros.integration;

import java.sql.SQLException;

import com.cerebros.models.Client;
import com.cerebros.models.Preferences;

public interface ClientDao {

	boolean emailExists(String email);

	void register(Client client, String password) throws SQLException;

	boolean login(String email, String password);

	Client getClient(String clientId);

	Client getClientByEmail(String email);

	void addClientPreferences(Preferences preferences);

	void updateClientPreferences(Preferences preferences);

	Preferences getClientPreferences(String clientId);

}
